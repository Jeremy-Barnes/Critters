package com.critters.dal.accessors;

import com.critters.dal.entity.QuestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Jeremy on 6/18/2018.
 */
public class QuestAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	QuestAccessor(EntityManager hibernateHelper){
		sql = hibernateHelper;
	}

	//todo caching
	public List<QuestInstance.StoryQuestStep> getStoryQuestStepConfigs() {
		List<QuestInstance.StoryQuestStep> dbItems = null;
		try {
			dbItems = sql.createQuery("from QuestInstance$StoryQuestStep").getResultList();
		}catch (PersistenceException ex) {
			logger.error("Database error searching for story quests!", ex);
		}
		return dbItems;
	}

	public QuestInstance.StoryQuestStep getStoryQuestStepConfig(int id) {
		QuestInstance.StoryQuestStep dbItem = null;
		try {
			dbItem = (QuestInstance.StoryQuestStep) sql.createQuery("from QuestInstance$StoryQuestStep where storyQuestStepID = :id").setParameter("id", id)
							  .getSingleResult();
		} catch (NoResultException nrex) {
			logger.info("No such story quest found with id " + id, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for id " + id, ex);
		}
		return dbItem;
	}

	public QuestInstance getQuestInstance(int id) {
		QuestInstance dbItem = null;
		try {
			dbItem = (QuestInstance) sql.createQuery("from QuestInstance where userQuestInstanceID = :id").setParameter("id", id)
													   .getSingleResult();
		} catch (NoResultException nrex) {
			logger.info("No such quest instance found with id " + id, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for id " + id, ex);
		}
		return dbItem;
	}

	public List<QuestInstance> getQuestInstances(int questUserID) {
		List<QuestInstance> dbItems = null;
		try {
			dbItems = sql.createQuery("from QuestInstance where userQuestInstanceID = :id").setParameter("id", questUserID)
										.getResultList();
		} catch (NoResultException nrex) {
			logger.info("No quests found for user " + questUserID, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for id " + questUserID, ex);
		}
		return dbItems;
	}

	public List<QuestInstance> getRandomQuestInstances(int questUserID, int npcID) {
		List<QuestInstance> dbItems = null;
		try {
			dbItems = sql.createQuery("from QuestInstance where userQuestInstanceID =:id and randomQuestGiverRedeemer = :npcID")
						 .setParameter("id", questUserID)
						 .setParameter("npcID", npcID)
						 .getResultList();
		} catch (NoResultException nrex) {
			logger.info("No quests found for user " + questUserID, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for id " + questUserID, ex);
		}
		return dbItems;
	}

	public List<QuestInstance> getStoryQuestInstances(int questUserID, int npcID) {
		List<QuestInstance> dbItems = null;
		try {
			dbItems = sql.createQuery("from QuestInstance q where q.currentStep is not null and q.questUserID = :id and (q.currentStep.redeemerNPC.npcID = :npcID or q.currentStep.giverNPC.npcID = :npcID)")
						 .setParameter("id", questUserID)
						 .setParameter("npcID", npcID)
						 .getResultList();
		} catch (NoResultException nrex) {
			logger.info("No quests found for user and npc " + questUserID + " & " + npcID, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for user and npc " + questUserID + " & " + npcID, ex);
		}
		return dbItems;
	}

	public List<QuestInstance.StoryQuestStep> getStoryQuestNextSteps(int npcID, int previousStepID) {
		List<QuestInstance.StoryQuestStep> dbItems = null;
		try {
			dbItems =  sql.createQuery("from QuestInstance$StoryQuestStep q where q.previousStep.storyQuestStepID = :id " +
											   "and q.giverNPC.npcID = :npcID")
						  .setParameter("id", previousStepID)
						  .setParameter("npcID", npcID)
						  .getResultList();
		} catch (NoResultException nrex) {
			logger.info("No such story quest found with previous step id " + previousStepID + " and giver npc id " + npcID, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for previous step id " + previousStepID + " and giver npc id " + npcID, ex);
		}
		return dbItems;
	}


	public <T> void save(List<T> quests) {
		quests.forEach(i -> save(i));
	}

	public <T> void save(T quest) {
		sql.merge(quest);
	}





}