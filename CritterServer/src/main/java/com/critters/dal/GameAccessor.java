package com.critters.dal;

import com.critters.dal.dto.entity.GameThumbnail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class GameAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	GameAccessor(EntityManager hibernateHelper){
		sql = hibernateHelper;
	}

	public List<GameThumbnail> getGameThumbnails(){//todo cache this for gods sake
		List<GameThumbnail> dbGames = null;
		try {
			dbGames = sql.createQuery("from GameThumbnail").getResultList();
		} catch (PersistenceException ex) {
			logger.error("No games found!", ex);
		}
		return dbGames;
	}

	public void save(List<GameThumbnail> games) {
		games.forEach(g -> save(g));
	}

	public void save(GameThumbnail game) {
		sql.merge(game);
	}
}
