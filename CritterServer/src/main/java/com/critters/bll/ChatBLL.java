package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.Message;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by Jeremy on 11/29/2016.
 */
public class ChatBLL {

	private static Map<Integer, AsyncResponse> listeners = Collections.synchronizedMap(new HashMap<Integer, AsyncResponse>());

	public static void createPoll(int userId, AsyncResponse asyncResponse){
		BackgroundJobManager.printLine("Entered createpoll " + Calendar.getInstance().getTime());
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				listeners.remove(userId);
				asyncResponse.resume(Response.status(Response.Status.NOT_FOUND)
											 .entity("Operation time out.").build());
			}
		});
		BackgroundJobManager.printLine("Timeouthandler set " + Calendar.getInstance().getTime());
		asyncResponse.setTimeout(30, TimeUnit.SECONDS);
		BackgroundJobManager.printLine("Timeout duration set  " + Calendar.getInstance().getTime());
		listeners.put(userId, asyncResponse);
		BackgroundJobManager.printLine("Leaving createpoll " + Calendar.getInstance().getTime());
	}

	public static void notify(int userId, Object notification){
		BackgroundJobManager.printLine("notify method entrance  " + Calendar.getInstance().getTime());
		if(listeners.containsKey(userId)) {
			listeners.get(userId).resume(notification);
			listeners.remove(userId);
		}
		BackgroundJobManager.printLine("notify method exit " + Calendar.getInstance().getTime());
	}

	public static Message sendMessage(Message message, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		BackgroundJobManager.printLine("Entered sendMessage method at  " + Calendar.getInstance().getTime());
		if (user.getUserID() == (message.getSender().getUserID())) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			entityManager.getTransaction().begin();

			Message mail = new Message(user, message.getRecipient(), false, Calendar.getInstance().getTime(), message.getMessageText(),
									   message.getMessageSubject(), message.getRootMessage(), message.getParentMessage());
			entityManager.persist(mail);
			entityManager.getTransaction().commit();
			entityManager.refresh(mail);
			entityManager.close();
			Message wiped = wipeSensitiveDetails(mail);
			BackgroundJobManager.printLine("Current time in before notify " + Calendar.getInstance().getTime());
			notify(message.getRecipient().getUserID(), wiped);
			return wiped;

		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	private static Message wipeSensitiveDetails(Message message) {
		UserBLL.wipeSensitiveFields(message.getSender());
		UserBLL.wipeSensitiveFields(message.getRecipient());
		return message;
	}

}
