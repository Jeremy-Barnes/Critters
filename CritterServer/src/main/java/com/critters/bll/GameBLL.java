package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.critters.dal.DAL;
import com.critters.dal.dto.GamesInfo;
import com.critters.dal.dto.entity.GameThumbnail;
import com.critters.dal.dto.entity.User;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy on 2/25/2017.
 */
public class GameBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static Map<Integer, String> userToGameToken = Collections.synchronizedMap(new HashMap<Integer, String>());


	public static GamesInfo getGames(){
		try(DAL dal = new DAL()) {
			List<GameThumbnail> dbGames= dal.games.getGameThumbnails();
			if(Extensions.isNullOrEmpty(dbGames)) return null;

			GamesInfo games = new GamesInfo();
			games.games = dbGames.toArray(new GameThumbnail[0]);
			return games;
		}
	}

	public static String createSinglePlayerSession(User player, String base64GameID){
		String[] keys = base64GameID.split("-");
		boolean tokenHasUserName = false;
		int gid = -1;
		for(String key : keys) {
			String plainKey = SecurityBLL.getFromBase64(key);
			try {
				 gid = Integer.parseInt(plainKey);
			} catch(NumberFormatException ex){ //thats no gameid
				if(player.getUserName().equalsIgnoreCase(key)) {
					tokenHasUserName = true;
				}
			}
		}

		if(gid != -1 && tokenHasUserName) {
			GameThumbnail dbGame = null;
			try(DAL dal = new DAL()) {
				dbGame = dal.games.getGameThumbnail(gid);
			}
			if(dbGame == null) {
				return null;
			}
			String guid = SecurityBLL.getGUID();
			String token = guid + "=" + System.currentTimeMillis() + "=" + gid;
			userToGameToken.put(player.getUserID(), token);
			return guid;
			//generate token here
		}
		return null;
	}

	public static int submitAndValidateGameScore(String gameTokenB64, User user){
		int winnings = 0;
		String gameToken = SecurityBLL.getFromBase64(gameTokenB64);
		String[] keys = gameToken.split("=");
		long submitTime = System.currentTimeMillis();

		String sessionToken = null;
		int gameTimeSeconds = 0;
		int gameScore = 0;
		String forensicToken = null;

		try {
			for (String key : keys) {
				if (key.contains("-")) {//guid
					sessionToken = key;
				} else if (key.contains("time")) { //time
					gameTimeSeconds = Integer.parseInt(key.replace("time", ""));
				} else if (key.contains("score")) {//score
					gameScore = Integer.parseInt(key.replace("score", ""));
				} else {//forensic token - all score events
					forensicToken = key;
				}
			}
		} catch(Exception e) { //invalid input from the user? Fuck em!
			return 0;
		}

		String storedGameToken = userToGameToken.get(user.getUserID());
		if(Extensions.isNullOrEmpty(sessionToken) || Extensions.isNullOrEmpty(storedGameToken)) return 0;
		keys = storedGameToken.split("=");
		if(!keys[0].equalsIgnoreCase(sessionToken)) return 0;
		int gid = Integer.parseInt(keys[2]);
		long serverGameTime = submitTime - Long.parseLong(keys[1]);

		GameThumbnail dbGame = null;
		try(DAL dal = new DAL()) {
			dbGame = dal.games.getGameThumbnail(gid);
		}
		winnings = gameScoreToCash(dbGame, gameScore, gameTimeSeconds*1000, serverGameTime, forensicToken, user);
		if(winnings > 0)
		user.setCritterbuxx(UserBLL.alterUserCash(user.getUserID(), winnings));
		return winnings;
	}

	private static int gameScoreToCash(GameThumbnail game, int clientScore, int clientTime, long serverTime, String movements, User user) {
		if(Math.abs(clientTime-serverTime) > 20000){
			logger.warn("A score was rejected because of a time discrepancy for user " + user.getUserName());
			return 0;
		}
		if(!Extensions.isNullOrEmpty(game.getScoreHandlerScript())) {
			try {
				ScriptEngineManager factory = new ScriptEngineManager();
				ScriptEngine engine = factory.getEngineByName("JavaScript");
				engine.eval(game.getScoreHandlerScript());
				Invocable inv = (Invocable) engine;
				ScriptObjectMirror o =(ScriptObjectMirror) inv.invokeFunction("handleScore", clientScore, movements);
				return Integer.parseInt("cashValue");
			} catch(Exception ex){
				logger.error("Couldn't execute the game score js for game " + game.getGameThumbnailConfigID(), ex);
			}
		} else if(game.getPointsToCurrencyFactor() == null) {
			return game.getPointsToCurrencyFactor() * clientScore;
		}
		return 0;
	}
}
