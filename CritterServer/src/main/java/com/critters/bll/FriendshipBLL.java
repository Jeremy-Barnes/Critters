package com.critters.bll;

import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.Friendship;
import com.critters.dal.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class FriendshipBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static Friendship createFriendship(User requester, User requestee, User loggedInUser){
		if(loggedInUser.getUserID() == (requester.getUserID())){
			Friendship request = new Friendship(loggedInUser, requestee, false, Calendar.getInstance().getTime());
			try (DAL dal = new DAL()){
				dal.beginTransaction();
				dal.friends.save(request);
				if(!dal.commitTransaction()) {
					logger.error("Friendship creation failed for friendship " + request.toString());
				} else {
					dal.friends.refresh(request);
				}
			}
			Friendship wiped = wipeSensitiveDetails(request);
			ChatBLL.notify(request.getRequested().getUserID(), null, wiped);
			return wiped;
		} else {
			logger.warn("An invalid requester was supplied while trying to send friend request for user " + requester.toString() + " \n session user:" + loggedInUser.toString());
		}
		return null;
	}

	public static Friendship acceptFriendRequest(Friendship request, User user) {
		if(user.getUserID() == request.getRequested().getUserID()) {

			Friendship dbReq = null;
			try(DAL dal = new DAL()) {
				dbReq = dal.friends.getFriendshipByIDs(request.getRequester().getUserID(), request.getRequested().getUserID());
				if(dbReq == null) return null;
				dbReq.setAccepted(true);
				dal.beginTransaction();
				dal.friends.save(dbReq);
				if(!dal.commitTransaction()) {
					logger.error("Friendship failed to accept " + request.toString());
				}
			}
			Friendship wiped = wipeSensitiveDetails(dbReq);
			ChatBLL.notify(request.getRequester().getUserID(), null, wiped);
			return wiped;
		} else {
			logger.warn("An invalid user was supplied while trying to accept friend request " + request.getRequested().toString() + " \n session user:" + user.toString());
			return null;
		}
	}

	public static void deleteFriendRequest(Friendship request, User user) {
		if(user.getUserID() == request.getRequested().getUserID()) {
			deleteFriendRequestWithIDs(request.getRequester().getUserID(), request.getRequested().getUserID());
		} else {
			logger.warn("An invalid user was supplied while trying to delete friend request" + request.toString() + " \n session user:" + user.toString());
		}
	}

	public static void cancelFriendRequest(Friendship request, User user) {
		if(user.getUserID() == request.getRequester().getUserID()) {
			deleteFriendRequestWithIDs(request.getRequester().getUserID(), request.getRequested().getUserID());
		} else {
			logger.warn("An invalid user was supplied while trying to cancel friend request" + request.toString() + " \n session user:" + user.toString());
		}
	}

	public static void endFriendship(Friendship friendship, User loggedInUser) {
		if(friendship.isAccepted() && (loggedInUser.getUserID() == friendship.getRequested().getUserID() || loggedInUser.getUserID() == friendship.getRequester().getUserID())){
			deleteFriendRequestWithIDs(friendship.getRequester().getUserID(), friendship.getRequested().getUserID());
		} else {
			logger.warn("An invalid user was supplied while trying to end friendship " + friendship.toString() + " \n session user:" + loggedInUser.toString());
		}
	}

	private static void deleteFriendRequestWithIDs(int requesterID, int requestedID) {
		try (DAL dal = new DAL()){
			Friendship dbReq = dal.friends.getFriendshipByIDs(requesterID, requestedID);
			dal.beginTransaction();
			dal.friends.delete(dbReq);
			if(!dal.commitTransaction()) {
				logger.error("Could not delete friend request with requester ID " + requesterID + " and requested ID " + requestedID);
			}
		}
	}

	private static Friendship wipeSensitiveDetails(Friendship f){
		UserBLL.wipeSensitiveFields(f.getRequested());
		UserBLL.wipeSensitiveFields(f.getRequester());
		return f;
	}
}
