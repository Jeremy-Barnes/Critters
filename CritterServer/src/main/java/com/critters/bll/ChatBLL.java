package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.Conversation;
import com.critters.dal.dto.Notification;
import com.critters.dal.dto.entity.Friendship;
import com.critters.dal.dto.entity.Item;
import com.critters.dal.dto.entity.Message;
import com.critters.dal.dto.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Created by Jeremy on 11/29/2016.
 */
public class ChatBLL {

	private static Map<Integer, AsyncResponse> listeners = Collections.synchronizedMap(new HashMap<Integer, AsyncResponse>());
	static final Logger logger = LoggerFactory.getLogger("application");

	public static void createPoll(int userId, AsyncResponse asyncResponse){
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				listeners.remove(userId);
				asyncResponse.resume(Response.status(Response.Status.NOT_FOUND)
											 .entity("Operation time out.").build());
			}
		});
		asyncResponse.setTimeout(30, TimeUnit.SECONDS);
		listeners.put(userId, asyncResponse);
	}
  
	public static void notify(int userId, Message message, Friendship friendRequest){
		logger.trace("Notifying user " + userId + "of " + message + " " + friendRequest);
		if(listeners.containsKey(userId)) {
			logger.trace("User " + userId + " found in listeners map");
			Notification notification = new Notification(message, friendRequest);
			listeners.get(userId).resume(Response.status(Response.Status.OK).entity(notification).build());
			listeners.remove(userId);
		} else {
			logger.trace("User " + userId + " not found in listeners map " + listeners.keySet());

		}
	}

	public static Message sendMessage(Message message, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		if (user.getUserID() == (message.getSender().getUserID())) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				entityManager.getTransaction().begin();
				Message mail = new Message(user, message.getRecipient(), false, true, true, false, Calendar.getInstance().getTime(), message.getMessageText(),
										   message.getMessageSubject(), message.getRootMessage(), message.getParentMessage());
				entityManager.persist(mail);
				entityManager.getTransaction().commit();
				entityManager.refresh(mail);
				entityManager.detach(mail);
				Message wiped = wipeSensitiveDetails(mail);
				notify(message.getRecipient().getUserID(), wiped, null);
				return wiped;
			} catch(Exception e) {
				logger.error("Could not send message to user " + message.toString() + "\n" + user.toString(), e);
				throw e;
			} finally {
				if(entityManager.getTransaction().isActive()){
					entityManager.getTransaction().rollback();
				}
				entityManager.close();
			}
		} else {
			logger.info("An invalid cookie was supplied for user " + user.toString());
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	public static List<Message> getMail(int userID, boolean undeliveredOnly) {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Message> mail = undeliveredOnly ?
					entityManager.createQuery("from Message where recipientUserId = :id and delivered = false").setParameter("id", userID).getResultList()
					: entityManager.createQuery("from Message where senderUserId = :id or recipientUserId = :id").setParameter("id", userID).getResultList();
			mail.forEach(m -> wipeSensitiveDetails(m));
			return mail;
		}catch(Exception e) {
			logger.error("Something went wrong with mail for user " + userID, e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	public static List<Conversation> getConversations(int userID) {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Message> mail = entityManager.createQuery("from Message where " +
																   "((senderUserId = :id and showSender = true) or " +
																   "(recipientUserId = :id and showRecipient = true)) and parentMessageId is null").setParameter("id", userID).getResultList();
			List<Message> mailChildren = entityManager.createQuery("from Message where ((senderUserId = :id and showSender = true) or " +
																		   "(recipientUserId = :id and showRecipient = true)) and rootMessageId in :ids").setHint(QueryHints.CACHE_MODE, CacheMode.REFRESH)
													  .setParameter("id", userID).setParameter("ids", mail.stream().map(Message::getMessageID).collect(Collectors.toList()))
													  .getResultList();
			return buildConversations(mail, mailChildren);
		} catch(Exception e) {
			logger.error("Something went wrong with messages for user " + userID, e);
			return null;
		} finally{
			entityManager.close();
		}
	}

	public static Message getMessage(int id, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			Message mail = (Message) entityManager.createQuery("from Message where messageID = :id").setParameter("id", id).getSingleResult();
			if ((user.getUserID() == mail.getSender().getUserID()) || (user.getUserID() == mail.getRecipient().getUserID())) {
				return mail;
			} else {
				throw new GeneralSecurityException("Invalid cookie supplied");
			}
		} catch(Exception e) {
			logger.error("Something went wrong with retrieval for message " + id + " for user " + user.getUserID(), e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	private static List<Message> getMessages(List<Integer> ids, User user) throws GeneralSecurityException {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Message> mail = (List<Message>) entityManager.createQuery("from Message where  messageID in :ids").setParameter("ids", ids).getResultList();
			for (Message m : mail) {
				if(!((user.getUserID() == m.getSender().getUserID()) || (user.getUserID() == m.getRecipient().getUserID()))) {
					throw new GeneralSecurityException("Invalid cookie supplied");
				}
			}
			return mail;
		} catch(Exception e) {
			logger.error("Something went wrong with retrieval for messages for user " + user.getUserID(), e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	public static void deleteMessage(int messageID, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		Message m = getMessage(messageID, user);
		if(m.getSender().getUserID() == user.getUserID()){
			m.setShowSender(false);
		}
		if(m.getRecipient().getUserID() == user.getUserID()){
			m.setShowRecipient(false);
		}

		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.merge(m);
			entityManager.getTransaction().commit();
		} catch(Exception e) {
			logger.error("Could not delete message " + messageID + " for user " + user.toString(), e);
			throw e;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static List<Message> markMessagesDelivered(List<Message> messages, User loggedInUser) throws GeneralSecurityException {
		messages = ChatBLL.getMessages(messages.stream().map(Message::getMessageID).collect(Collectors.toList()), loggedInUser);
		if(messages != null) {
			messages.forEach(m -> m.setDelivered(true));
			messages = updateMessages(messages);
			messages.forEach(m -> wipeSensitiveDetails(m));
		}
		return messages;
	}

	public static List<Message> markMessagesRead(List<Message> messages, User loggedInUser) throws GeneralSecurityException {
		messages = ChatBLL.getMessages(messages.stream().map(Message::getMessageID).collect(Collectors.toList()), loggedInUser);
		if(messages != null) {
			messages.forEach(m -> m.setRead(true));
			messages = updateMessages(messages);
			messages.forEach(m -> wipeSensitiveDetails(m));
		}
		return messages;
	}

	private static List<Message> updateMessages(List<Message> messages){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			entityManager.getTransaction().begin();
			for(Message m : messages) {
				entityManager.merge(m);
			}
			entityManager.getTransaction().commit();
		} catch(Exception e) {
			String messageArray = "";
			for(Message message : messages){
				messageArray += "\n" + message.toString();
			}
			logger.error("Could not update messages " + messageArray, e);
			throw e;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
		return messages;
	}

	private static Message wipeSensitiveDetails(Message message) {
		UserBLL.wipeSensitiveFields(message.getSender());
		UserBLL.wipeSensitiveFields(message.getRecipient());
		return message;
	}

	private static List<Conversation> buildConversations(List<Message> roots, List<Message> children){
		List<Conversation> conversations = new ArrayList<Conversation>();
		for(Message m : roots){
			List<Message> conversation = new ArrayList<Message>();
			conversation.add(m);
			conversation.addAll(children.stream()
										.filter(msg -> msg.getRootMessage() != null && msg.getRootMessage().getMessageID() == m.getMessageID())
										.sorted((o1, o2) -> o1.getMessageID() > o2.getMessageID() ? -1 : 1)
										.collect(Collectors.toList()));

			List<User> senders = conversation.parallelStream().map(Message::getSender).collect(Collectors.toList());
			List<User> receivers = conversation.parallelStream().map(Message::getRecipient).collect(Collectors.toList());
			senders.addAll(receivers);
			List<User> participants = senders.stream().distinct().collect(Collectors.toList());
			participants.forEach(u -> UserBLL.wipeSensitiveFields(u));
			conversation.forEach(c -> c.nullUsersOut());
			conversation.forEach(c -> c.nullParentsOut());
			Conversation convo = new Conversation();
			convo.messages = conversation;//.toArray(new Message[0]);
			convo.participants = participants;//.toArray(new User[0]);
			conversations.add(convo);
		}
		return conversations;
	}

}
