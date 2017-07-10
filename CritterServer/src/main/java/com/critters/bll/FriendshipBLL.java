package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.Friendship;
import com.critters.dal.dto.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Calendar;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class FriendshipBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static Friendship createFriendship(User requester, User requestee, User loggedInUser) throws Exception{
		if(loggedInUser.getUserID() == (requester.getUserID())){
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			entityManager.getTransaction().begin();
			Friendship request = new Friendship(loggedInUser, requestee, false, Calendar.getInstance().getTime());
			try {
				entityManager.persist(request);
				entityManager.getTransaction().commit();
				entityManager.refresh(request);
			} catch(Exception e) {
				logger.error("Friendship creation failed for friendship " + request.toString(), e);
				throw new Exception("Friend request failed!");
			} finally {
				if(entityManager.getTransaction().isActive()){
					entityManager.getTransaction().rollback();
				}
				entityManager.close();
			}
			Friendship wiped = wipeSensitiveDetails(request);
			ChatBLL.notify(request.getRequested().getUserID(), null, wiped);
			return wiped;
		} else {
			logger.info("An invalid cookie was supplied for user " + loggedInUser.toString());
			throw new GeneralSecurityException("Invalid cookie supplied, try logging out and logging back in.");
		}
	}

	public static Friendship acceptFriendRequest(Friendship request, User user) throws Exception {
		if(user.getUserID() == request.getRequested().getUserID()) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

			entityManager.getTransaction().begin();
			Friendship dbReq;
			try {
				dbReq = (Friendship) entityManager.createQuery("from Friendship where requesterUserID = :req and requestedUserID = :friend")
															 .setParameter("req", request.getRequester().getUserID()).setParameter("friend", request.getRequested().getUserID())
															 .getSingleResult();
				dbReq.setAccepted(true);
				entityManager.getTransaction().commit();
			} catch(Exception e) {
				logger.error("Friendship failed to accept " + request.toString(), e);
				throw new Exception("Friendship creation failed!");
			} finally {
				if(entityManager.getTransaction().isActive()){
					entityManager.getTransaction().rollback();
				}
				entityManager.close();
			}
			Friendship wiped = wipeSensitiveDetails(dbReq);
			ChatBLL.notify(request.getRequester().getUserID(), null, wiped);
			return wiped;
		} else {
			logger.info("An invalid cookie was supplied for user " + user.toString());
			throw new GeneralSecurityException("Invalid cookie supplied, try logging out and logging back in.");
		}
	}

	public static void deleteFriendRequest(Friendship request, User user) throws Exception {
		if(user.getUserID() == request.getRequested().getUserID()) {
			deleteFriendRequestWithIDs(request.getRequester().getUserID(), request.getRequested().getUserID());
		} else {
			logger.info("An invalid cookie was supplied for user " + user.toString());
			throw new GeneralSecurityException("Invalid cookie supplied, try logging out and logging back in.");
		}
	}

	public static void cancelFriendRequest(Friendship request, User user) throws Exception {
		if(user.getUserID() == request.getRequester().getUserID()) {
			deleteFriendRequestWithIDs(request.getRequester().getUserID(), request.getRequested().getUserID());
		} else {
			logger.info("An invalid cookie was supplied for user " + user.toString());
			throw new GeneralSecurityException("Invalid cookie supplied, try logging out and logging back in.");
		}
	}

	public static void endFriendship(Friendship friendship, User loggedInUser) throws Exception {
		if(friendship.isAccepted() && (loggedInUser.getUserID() == friendship.getRequested().getUserID() || loggedInUser.getUserID() == friendship.getRequester().getUserID())){
			deleteFriendRequestWithIDs(friendship.getRequester().getUserID(), friendship.getRequested().getUserID());
		} else {
			logger.info("An invalid cookie was supplied for user " + loggedInUser.toString());
			throw new GeneralSecurityException("Invalid cookie supplied, try logging out and logging back in.");
		}
	}

	private static void deleteFriendRequestWithIDs(int requesterID, int requestedID) throws Exception {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		entityManager.getTransaction().begin();
		try {
			Friendship dbReq = (Friendship) entityManager.createQuery("from Friendship where requesterUserID = :req and requestedUserID = :friend")
														 .setParameter("req", requesterID).setParameter("friend", requestedID)
														 .getSingleResult();
			entityManager.remove(dbReq);
			entityManager.getTransaction().commit();
		} catch(Exception e) {
			logger.error("Could not delete friend request with requester ID " + requesterID + " and requested ID " + requestedID, e);
			throw new Exception("Couldn't end this friendship for an unknown reason. Please tell an admin.");
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	private static Friendship wipeSensitiveDetails(Friendship f){
		UserBLL.wipeSensitiveFields(f.getRequested());
		UserBLL.wipeSensitiveFields(f.getRequester());
		return f;
	}
}
