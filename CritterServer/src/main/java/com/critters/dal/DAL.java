package com.critters.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class DAL implements AutoCloseable {
	static final Logger logger = LoggerFactory.getLogger("application");
	public EntityManager sql;
	public ItemAccessor items;
	public UserAccessor users;
	public PetAccessor pets;
	public ConfigAccessor configuration;
	public StoreAccessor shops;
	public ChatAccessor messages;
	public FriendAccessor friends;
	public GameAccessor games;
	public NPCAccessor npcs;

	public DAL(){
		sql =  HibernateUtil.getEntityManagerFactory().createEntityManager();
		items = new ItemAccessor(sql);
		users = new UserAccessor(sql);
		pets = new PetAccessor(sql);
		configuration = new ConfigAccessor(sql);
		shops = new StoreAccessor(sql);
		messages = new ChatAccessor(sql);
		friends = new FriendAccessor(sql);
		games = new GameAccessor(sql);
		npcs = new NPCAccessor(sql);
	}

	public void beginTransaction(){
			if(!sql.getTransaction().isActive())
				sql.getTransaction().begin();
	}

	public boolean commitTransaction(){
		try {
			if (sql.getTransaction().isActive()) {
				sql.getTransaction().commit();
				return true;
			}
		} catch(Exception e) {
			rollback();
			logger.error("Transaction failed", e);
		}
		return false;
	}

	public void rollback(){
		if (sql.getTransaction().isActive())
			sql.getTransaction().rollback();
	}

	@Override
	public void close(){
		rollback();
		sql.close();
	}
}
