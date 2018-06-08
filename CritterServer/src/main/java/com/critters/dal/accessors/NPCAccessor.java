package com.critters.dal.accessors;

import com.critters.dal.entity.NPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class NPCAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	NPCAccessor(EntityManager hibernateHelper){
		sql = hibernateHelper;
	}

	//todo caching
	public List<NPC> getNPCs() {
		List<NPC> dbItems = null;
		try {
			dbItems = sql.createQuery("from NPC").getResultList();
		}catch (PersistenceException ex) {
			logger.error("Database error searching for npcs!", ex);
		}
		return dbItems;
	}

	public NPC getNPC(int id) {
		NPC dbItem = null;
		try {
			dbItem = (NPC) sql.createQuery("from NPC where npcID = :id").setParameter("id", id)
										  .getSingleResult();
		} catch (NoResultException nrex) {
			logger.info("No such npc found with id " + id, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for id " + id, ex);
		}
		return dbItem;
	}

	public void save(List<NPC> npcs) {
		npcs.forEach(i -> save(i));
	}

	public void save(NPC npc) {
		sql.merge(npc);
	}





}
