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
		if(loggedInUser.getUserID() == (requester.getUserID())){
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			entityManager.getTransaction().begin();

			Friendship request = new Friendship(loggedInUser, requestee, false, Calendar.getInstance().getTime());
			entityManager.persist(request);
			entityManager.getTransaction().commit();
			entityManager.close();
		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}

	}

	public static void acceptFriendRequest(Friendship request, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		if(user.getUserID() == request.getRequested().getUserID()) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

			entityManager.getTransaction().begin();
			Friendship dbReq = (Friendship) entityManager.createQuery("from Friendship where requesterUserID = :req and requestedUserID = :friend")
														 .setParameter("req", request.getRequester().getUserID()).setParameter("friend", request.getRequested().getUserID())
														 .getSingleResult();
			dbReq.setAccepted(true);
			entityManager.getTransaction().commit();
			entityManager.close();
			request.setFriendshipID(dbReq.getFriendshipID());
		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	public static void deleteFriendRequest(Friendship request, User user) throws GeneralSecurityException, UnsupportedEncodingException {
		if(user.getUserID() == request.getRequested().getUserID()) {
			deleteFriendRequestWithIDs(request.getRequester().getUserID(), request.getRequested().getUserID());
		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	public static void cancelFriendRequest(Friendship request, User user)  throws GeneralSecurityException, UnsupportedEncodingException {
		if(user.getUserID() == request.getRequester().getUserID()) {
			deleteFriendRequestWithIDs(request.getRequester().getUserID(), request.getRequested().getUserID());
		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	public static void endFriendship(Friendship friendship, User loggedInUser) throws GeneralSecurityException, UnsupportedEncodingException {
		if(friendship.isAccepted() && (loggedInUser.getUserID() == friendship.getRequested().getUserID() || loggedInUser.getUserID() == friendship.getRequester().getUserID())){
			deleteFriendRequestWithIDs(friendship.getRequester().getUserID(), friendship.getRequested().getUserID());
		} else {
			throw new GeneralSecurityException("Invalid cookie supplied");
		}
	}

	private static void deleteFriendRequestWithIDs(int requesterID, int requestedID){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		entityManager.getTransaction().begin();
		Friendship dbReq = (Friendship) entityManager.createQuery("from Friendship where requesterUserID = :req and requestedUserID = :friend")
													 .setParameter("req", requesterID).setParameter("friend", requestedID)
													 .getSingleResult();
		entityManager.remove(dbReq);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
