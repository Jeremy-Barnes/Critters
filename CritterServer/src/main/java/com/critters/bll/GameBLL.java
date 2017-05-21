package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.GamesInfo;
import com.critters.dal.dto.entity.GameThumbnail;
import com.critters.dal.dto.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Jeremy on 2/25/2017.
 */
public class GameBLL {

	static final Logger logger = LoggerFactory.getLogger(GameBLL.class);


	public static GamesInfo getGames(){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
//todo cache this for gods sake
		try {
			List<Item> dbGames= entityManager.createQuery("from GameThumbnail").getResultList();
			GamesInfo games = new GamesInfo();
			games.games = dbGames.toArray(new GameThumbnail[0]);
			return games;
		} catch (PersistenceException ex) {
			logger.error("Couldn't retrieve games", ex);
			return null; //no item found
		} finally {
			entityManager.close();
		}
	}
}
