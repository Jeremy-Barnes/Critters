package com.critters.dal.accessors;

import com.critters.dal.entity.Message;
import com.critters.dal.entity.User;
import org.hibernate.CacheMode;
import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Jeremy on 1/3/2018.
 */
public class ChatAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;


	public ChatAccessor(EntityManager helper){
		sql = helper;
	}

	public List<Message> getMailByUserID(int id, boolean undeliveredOnly){
		List<Message> mail = null;
		try {
			mail = undeliveredOnly ?
					sql.createQuery("from Message where recipientUserId = :id and delivered = false").setParameter("id", id).getResultList()
					: sql.createQuery("from Message where senderUserId = :id or recipientUserId = :id").setParameter("id", id).getResultList();
		} catch (PersistenceException ex) {
			logger.error("Something went wrong with mail for user " + id, ex);
		}
		return mail;
	}

	public Message getMessageByID(int id) {
		Message mail = null;
		try {
			mail = (Message) sql.createQuery("from Message where messageID = :id").setParameter("id", id).getSingleResult();
		} catch(NoResultException nrex){ //no such message
			logger.debug("Failed to retrieve message for invalid id " + id, nrex);
		}
		return mail;
	}

	public List<Message> getMessagesByIDs(List<Integer> ids) {
		List<Message> mail = null;
		try {
			mail = (List<Message>) sql.createQuery("from Message where messageId in :ids").setParameter("ids", ids).getResultList();
		} catch(NoResultException nrex){ //no such message
			String itemArray = "";
			for(Integer item : ids){
				itemArray += "\n" + item;
			}
			logger.error("Something went wrong with retrieval for messages " + itemArray, nrex);
		}
		return mail;
	}

	public List<Message> getRootMessages(int userID){
		List<Message> mail = null;
		try {
			mail = sql.createQuery("from Message where " +
									"((senderUserId = :id and showSender = true) or " +
									"(recipientUserId = :id and showRecipient = true)) " +
									"and parentMessageId is null")
			   .setHint(QueryHints.CACHE_MODE, CacheMode.REFRESH)
			   .setParameter("id", userID).getResultList();
		} catch(Exception e) {
			logger.error("Something went wrong with messages for user " + userID, e);
		}
		return mail;
	}

	public List<Message> getChildrenOfRoots(List<Integer> rootMessageIDs, int userID) {
		List<Message> mailChildren = null;
		try {
			mailChildren = sql.createQuery("from Message where ((senderUserId = :id and showSender = true) or " +
												   "(recipientUserId = :id and showRecipient = true)) " +
												   "and rootMessageId in :ids")
							  .setHint(QueryHints.CACHE_MODE, CacheMode.REFRESH)
							  .setParameter("id", userID)
							  .setParameter("ids", rootMessageIDs)
							  .getResultList();
		} catch(Exception e) {
			String itemArray = "";
			for(Integer item : rootMessageIDs){
				itemArray += "\n" + item;
			}
			logger.error("Something went wrong with retrieving children of messages for user" + userID + itemArray, e);
		}
		return mailChildren;
	}


	public List<User> search(String searchTerm){
		StoredProcedureQuery query = sql.createStoredProcedureQuery("usersearch", User.class);
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.setParameter(1, "%" + searchTerm + "%");
		query.execute();
		return query.getResultList();
	}

	public void save(List<Message> messages) {
		messages.forEach(m -> save(m));
	}

	public void save(Message messages) {
		sql.merge(messages);
	}

	public void refresh(Message message){
		sql.refresh(message);
	}
}
