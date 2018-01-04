package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.critters.dal.DAL;
import com.critters.dal.dto.GamesInfo;
import com.critters.dal.dto.entity.GameThumbnail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Jeremy on 2/25/2017.
 */
public class GameBLL {

	static final Logger logger = LoggerFactory.getLogger("application");


	public static GamesInfo getGames(){
		try(DAL dal = new DAL()) {
			List<GameThumbnail> dbGames= dal.games.getGameThumbnails();
			if(Extensions.isNullOrEmpty(dbGames)) return null;

			GamesInfo games = new GamesInfo();
			games.games = dbGames.toArray(new GameThumbnail[0]);
			return games;
		}
	}
}
