package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.Message;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Calendar;

/**
 * Created by Jeremy on 11/29/2016.
 */
public class ChatBLL {

	public static Message sendMessage(Message message, User user) throws GeneralSecurityException, UnsupportedEncodingException {

		if (user.getUserID() == (message.getSender().getUserID())) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			entityManager.getTransaction().begin();

			Message mail = new Message(user, message.getRecipient(), false, Calendar.getInstance().getTime(), message.getMessageText(), message.getMessageSubject());
			entityManager.persist(mail);
			entityManager.getTransaction().commit();
			entityManager.refresh(mail);
			entityManager.close();
			return wipeSensitiveDetails(mail);
		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	private static Message wipeSensitiveDetails(Message message) {
		return message;
	}

}
