package com.critters.games.sockets;

import com.critters.dal.dto.Twople;
import com.critters.dal.dto.entity.User;
import com.critters.games.GameController;
import com.critters.games.pong.PongController;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.*;


/**
 * Created by Jeremy on 8/15/2016.
 */
public class SocketManager {

	/***
	 * These are the various associations of running multiplayer games. They all represent the same sets of data, but organized differently for faster access based on
	 * intention. Indexed on clientID, gameType, and gameID.
	 */
	public static Map<String,Twople<User, GameController>> clientIDToPlayer = Collections.synchronizedMap(new HashMap<String,Twople<User, GameController>>());

	public static Map<Integer,List<GameController>> gameTypeToRunningGames = Collections.synchronizedMap(new HashMap<Integer, List<GameController>>());
	public static Map<String,GameController> gameIDToGame = Collections.synchronizedMap(new HashMap<String, GameController>());

	/**
	 * AJAX called, not websocketed
	 * @param gameType - Integer ID for game type
	 * @param clientID - host client ID, NOT THE USER ID.
	 * @param gameName - 2v2 NR 30 Min
	 * @return The unique gameID that other people can use to connect to your game
	 */
	public static String createNewGame(int gameType, String clientID, String gameName){
		if(clientIDToPlayer.containsKey(clientID) && gameType >= 0) {
			Twople<User, GameController> host = clientIDToPlayer.get(clientID);

			String gameInstanceID = System.currentTimeMillis() + ""; //todo real id here

			GameController gameInstance = getGame(gameType, gameInstanceID, gameName, clientID);
			gameIDToGame.put(gameInstanceID, gameInstance);
			host.y = gameInstance;
			if (gameTypeToRunningGames.containsKey(gameType)) {
				gameTypeToRunningGames.get(gameType).add(gameInstance);
			} else {
				ArrayList gameList = new ArrayList();
				gameList.add(gameInstance);
				gameTypeToRunningGames.put(gameType, gameList);
			}
			return gameInstanceID;
		}
		return null;
	}

	/***
	 * AJAX called, not websocketed
	 * @param gameInstanceID - Unique game lobby ID to join
	 * @param clientID - NOT USER ID, client ID of person trying to join game
	 * @param asyncResponse - AJAX response that will be resumed once host decides if client can join (or not)
	 */
	public static void tryToConnect(String gameInstanceID, String clientID, AsyncResponse asyncResponse){
		if(gameIDToGame.containsKey(gameInstanceID) && clientIDToPlayer.containsKey(clientID)) {
			User user = clientIDToPlayer.get(clientID).x;
			gameIDToGame.get(gameInstanceID).askForConnectPermission(clientID, user, asyncResponse);
		} else {
			asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
		}
	}

	public static String findGames(int gameID, String userName){
		return (String) gameIDToGame.keySet().toArray()[0];
	}

	public static List<GameController> findGames(int gameID, int userID){
		return null;
	}

	public static List<GameController> findAllGames(int gameID){
		return null;
	}

	public static void  setUserID(String secure, User user) {
		clientIDToPlayer.put(secure, new Twople<User,GameController>(user, null));
	}

	private static GameController getGame(int gameType, String gameInstanceID, String title, String hostClientId) {
		switch(gameType){
			case 0: return new PongController(gameInstanceID, title, hostClientId, gameType);
		}
		return null;
	}
}


