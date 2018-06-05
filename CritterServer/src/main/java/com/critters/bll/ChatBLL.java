package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.critters.dal.accessors.DAL;
import com.critters.dto.Conversation;
import com.critters.dto.Notification;
import com.critters.dal.entity.Friendship;
import com.critters.dal.entity.Message;
import com.critters.dal.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
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

	public static Message sendMessage(Message message, User user) {
		Message mail = null;
		if(user.getUserID() == (message.getSender().getUserID())) {
			mail = new Message(user, message.getRecipient(), false, true, true, false, Calendar.getInstance().getTime(), message.getMessageText(),
									   message.getMessageSubject(), message.getRootMessage(), message.getParentMessage());

			try(DAL dal = new DAL()) {
				dal.beginTransaction();
				dal.messages.save(mail);
				if(!dal.commitTransaction()) {
					logger.error("Could not send message to user " + message.toString() + "\n" + user.toString());
					mail = null;
				} else {
					dal.messages.refresh(mail);
				}
			}
			notify(message.getRecipient().getUserID(), wipeSensitiveDetails(mail), null);
		} else {
			logger.warn("An invalid message/user pair was supplied while trying to send message for user " + message.toString() + " \n" + user.toString());
		}
		return mail;
	}

	public static List<Message> getMail(int userID, boolean undeliveredOnly)  {
		List<Message> mail;
		try(DAL dal = new DAL()) {
			mail = dal.messages.getMailByUserID(userID, undeliveredOnly);
		}
		if(!Extensions.isNullOrEmpty(mail))
			mail.forEach(m -> wipeSensitiveDetails(m));
		return mail;
	}

	public static List<Conversation> getConversations(int userID) {
		List<Message> mail = null;
		List<Message> mailChildren = null;
		try(DAL dal = new DAL()) {
			mail = dal.messages.getRootMessages(userID);
			mailChildren = dal.messages.getChildrenOfRoots(mail.stream().map(Message::getMessageID).collect(Collectors.toList()), userID);
		}
		return buildConversations(mail, mailChildren);
	}

	public static Message getMessage(int id, User user) {
		Message mail;
		try(DAL dal = new DAL()) {
			mail = getMessage(id, user, dal);
		}
		return mail;
	}

	private static Message getMessage(int id, User user, DAL dal){
		Message mail = dal.messages.getMessageByID(id);

		if(mail != null && !((user.getUserID() == mail.getSender().getUserID()) || (user.getUserID() == mail.getRecipient().getUserID()))) {
			logger.info("Impersonation attempt, someone tried to get message " + id + " who wasn't party to the conversation, user: " + user.toString());
			mail = null;
		}
		return mail;
	}

	private static List<Message> getMessages(List<Integer> messageIds, User user, DAL dal) {
		List<Message> mail =  dal.messages.getMessagesByIDs(messageIds);

		for (Message m : mail) {
			if(!((user.getUserID() == m.getSender().getUserID()) || (user.getUserID() == m.getRecipient().getUserID()))) {
				logger.info("Impersonation attempt, someone tried to get message " + m.getMessageID() + " who wasn't party to the conversation, user: " + user.toString());
				mail = null;
				break;
			}
		}
		return mail;
	}

	public static void deleteMessages(List<Integer> messageIDs, User user) {
		try(DAL dal = new DAL()) {
			List<Message> messages = getMessages(messageIDs, user, dal);
			if(Extensions.isNullOrEmpty(messages)) return;
			for(Message m : messages) {
				if (m.getSender().getUserID() == user.getUserID()) {
					m.setShowSender(false);
				} else if (m.getRecipient().getUserID() == user.getUserID()) {
					m.setShowRecipient(false);
				}
			}

			dal.beginTransaction();
			dal.messages.save(messages);
			if(!dal.commitTransaction()) {
				logger.error("Could not delete messages for user " + user.toString());
			}
		}
	}

	public static void deleteMessage(int messageID, User user) {
		try(DAL dal = new DAL()) {
			Message m = getMessage(messageID, user, dal);
			if (m.getSender().getUserID() == user.getUserID()) {
				m.setShowSender(false);
			}
			if (m.getRecipient().getUserID() == user.getUserID()) {
				m.setShowRecipient(false);
			}
			dal.beginTransaction();
			dal.messages.save(m);
			if (!dal.commitTransaction()) {
				logger.error("Could not delete message " + messageID + " for user " + user.toString());
			}
		}
	}

	public static List<Message> markMessagesDelivered(List<Message> messages, User loggedInUser) {
		try(DAL dal = new DAL()) {
			messages = getMessages(messages.stream().map(Message::getMessageID).collect(Collectors.toList()), loggedInUser, dal);
			if (!Extensions.isNullOrEmpty(messages)) {
				for (Message m : messages) {
					if(m.getRecipient().getUserID() == loggedInUser.getUserID()) return null;
					m.setDelivered(true);
				}
				dal.beginTransaction();
				dal.messages.save(messages);
				if(!dal.commitTransaction()) {
					String itemArray = "";
					for(Message item : messages){
						itemArray += "\n" + item;
					}
					logger.error("Couldn't mark these messages as delivered for user" + loggedInUser.toString(), itemArray);
				}
			}
		}
		messages.forEach(m -> wipeSensitiveDetails(m));
		return messages;
	}

	public static List<Message> markMessagesRead(List<Message> messages, User loggedInUser) {
		try(DAL dal = new DAL()) {
			messages = getMessages(messages.stream().map(Message::getMessageID).collect(Collectors.toList()), loggedInUser, dal);
			if (!Extensions.isNullOrEmpty(messages)) {
				for (Message m : messages) {
					if (m.getRecipient().getUserID() == loggedInUser.getUserID()) return null;
					m.setRead(true);
				}
				dal.beginTransaction();
				dal.messages.save(messages);
				if(!dal.commitTransaction()) {
					String itemArray = "";
					for(Message item : messages){
						itemArray += "\n" + item;
					}
					logger.error("Couldn't mark these messages as read for user" + loggedInUser.toString(), itemArray);
				}
			}
		}
		messages.forEach(m -> wipeSensitiveDetails(m));
		return messages;
	}

	public static List<Message> markMessagesUnread(List<Message> messages, User loggedInUser) {
		try (DAL dal = new DAL()) {
			messages = getMessages(messages.stream().map(Message::getMessageID).collect(Collectors.toList()), loggedInUser, dal);
			if (!Extensions.isNullOrEmpty(messages)) {
				for (Message m : messages) {
					if (m.getRecipient().getUserID() == loggedInUser.getUserID()) return null;
					m.setRead(false);
				}
				dal.beginTransaction();
				dal.messages.save(messages);
				if (!dal.commitTransaction()) {
					String itemArray = "";
					for (Message item : messages) {
						itemArray += "\n" + item;
					}
					logger.error("Couldn't mark these messages as unread for user" + loggedInUser.toString(), itemArray);
				}
			}
		}
		messages.forEach(m -> wipeSensitiveDetails(m));
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
