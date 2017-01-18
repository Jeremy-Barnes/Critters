package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.Conversation;

import com.critters.dal.dto.entity.Message;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by Jeremy on 11/29/2016.
 */
public class ChatBLL {

	private static Map<Integer, AsyncResponse> listeners = Collections.synchronizedMap(new HashMap<Integer, AsyncResponse>());

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

	public static void notify(int userId, Object notification){
		if(listeners.containsKey(userId)) {
			listeners.get(userId).resume(notification);
			listeners.remove(userId);
		}
	}

	public static Message sendMessage(Message message, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		if (user.getUserID() == (message.getSender().getUserID())) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				entityManager.getTransaction().begin();

				Message mail = new Message(user, message.getRecipient(), false, Calendar.getInstance().getTime(), message.getMessageText(),
										   message.getMessageSubject(), message.getRootMessage(), message.getParentMessage());
				entityManager.persist(mail);
				entityManager.getTransaction().commit();
				entityManager.refresh(mail);
				entityManager.detach(mail);
				Message wiped = wipeSensitiveDetails(mail);
				notify(message.getRecipient().getUserID(), wiped);
				return wiped;
			} finally {
				entityManager.close();
			}
		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	public static List<Message> getMail(int userID, boolean unreadOnly) {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Message> mail = unreadOnly ?
					entityManager.createQuery("from Message where senderUserId = :id or recipientUserId = :id and read = false").setParameter("id", userID).getResultList()
					: entityManager.createQuery("from Message where senderUserId = :id or recipientUserId = :id").setParameter("id", userID).getResultList();

			entityManager.getTransaction().begin();
			mail.forEach(m->m.setRead(true));
			mail.forEach(m->entityManager.merge(m));
			entityManager.getTransaction().commit();
			mail.forEach(m -> wipeSensitiveDetails(m));
			return mail;
		} finally {
			entityManager.close();
    }
	}


	public static List<Conversation> getConversations(int userID) {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Message> mail = entityManager.createQuery("from Message where senderUserId = :id or recipientUserId = :id").setParameter("id", userID).getResultList();
			List<Message> mailChildren = entityManager.createQuery("from Message where rootMessageId in :ids")
													  .setParameter("ids", mail.stream().map(Message::getMessageID).collect(Collectors.toList()))
													  .getResultList();
			entityManager.getTransaction().begin();
			mail.forEach(m->m.setRead(true));
			mailChildren.forEach(m -> m.setRead(true));
			mail.forEach(m->entityManager.merge(m));
			mailChildren.forEach(m -> entityManager.merge(m));
			entityManager.getTransaction().commit();
			return buildConversations(mail, mailChildren);
		} finally {
		entityManager.close();
		}
	}

	public static Message getMessage(int id, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			Message mail = (Message) entityManager.createQuery("from Message where messageID = :id").setParameter("id", id).getSingleResult();
			mail.setRead(true);
			entityManager.merge(mail);
			if ((user.getUserID() == mail.getSender().getUserID()) || (user.getUserID() == mail.getRecipient().getUserID())) {
				entityManager.detach(mail);
				Message wiped = wipeSensitiveDetails(mail);
				notify(mail.getRecipient().getUserID(), wiped);
				return wiped;
			} else {
				throw new GeneralSecurityException("Invalid cookie supplied");
			}
		} finally {
			entityManager.close();
		}
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
