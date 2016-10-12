package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.Friendship;
import com.critters.dal.dto.entity.User;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Calendar;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class FriendshipBLL {

	public static void createFriendship(User requester, User requestee, User loggedInUser) throws GeneralSecurityException, UnsupportedEncodingException {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		if(loggedInUser.getEmailAddress().equalsIgnoreCase(requester.getEmailAddress())){
			entityManager.getTransaction().begin();

			Friendship request = new Friendship(loggedInUser, requestee, false, Calendar.getInstance().getTime());
			entityManager.persist(request);
			entityManager.getTransaction().commit();

		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}

		entityManager.close();
	}

	public static void acceptFriendRequest(Friendship request, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		if(user.getUserID() == request.getRequested().getUserID()) {
			entityManager.getTransaction().begin();
			Friendship dbReq = (Friendship) entityManager.createQuery("from Friendship where requesterUserID = :req and requestedUserID = :friend")
														 .setParameter("req", request.getRequester().getUserID()).setParameter("friend", request.getRequested().getUserID())
														 .getSingleResult();
			dbReq.setAccepted(true);
			entityManager.getTransaction().commit();
			entityManager.close();
			request.setFriendshipID(dbReq.getFriendshipID());
		} else {
			entityManager.close();
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	public static void deleteFriendRequest(Friendship request, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		if(user.getUserID() == request.getRequested().getUserID()) {
			entityManager.getTransaction().begin();
			Friendship dbReq = (Friendship) entityManager.createQuery("from Friendship where requesterUserID = :req and requestedUserID = :friend")
														 .setParameter("req", request.getRequester().getUserID()).setParameter("friend", request.getRequested().getUserID())
														 .getSingleResult();
			entityManager.remove(dbReq);
			entityManager.getTransaction().commit();
			entityManager.close();
		} else {
			entityManager.close();
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}
}
