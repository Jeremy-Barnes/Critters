package com.critters.dal;

import com.critters.dal.dto.entity.Friendship;
import com.critters.dal.dto.entity.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class FriendAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	FriendAccessor(EntityManager hibernateHelper){
		sql = hibernateHelper;
	}

	public Friendship getFriendshipByID(int id){
		Friendship friendship = null;
		try {
			friendship = (Friendship) sql.createQuery("from Friendship where friendshipID = :id")
									  .setParameter("id", id)
									  .getSingleResult();
		} catch (PersistenceException ex) {
			logger.error("No friendship found for id " + id, ex);
		}
		return friendship;
	}

	public Friendship getFriendshipByIDs(int requesterID, int requesteeID){
		Friendship dbReq = null;
		try {
			dbReq = (Friendship) sql.createQuery("from Friendship where requesterUserID = :req and requestedUserID = :friend")
											  .setParameter("req", requesterID).setParameter("friend", requesteeID)
											  .getSingleResult();
		} catch (PersistenceException ex) {
			logger.error("No friendship found for users ids" + requesterID + " " + requesteeID, ex);
		}
		return dbReq;
	}

	public boolean isPetNameTaken(String petName){
		try {
			return !(boolean) sql.createNativeQuery("SELECT EXISTS(SELECT 1 from pets where petName = ?1)")
											.setParameter(1, petName)
											.getSingleResult();
		} catch(Exception e) {
			logger.error("This pet name so shit it blew up the server? " + petName, e);
			return true;
		}
	}

	public void refresh(Friendship friendship){
		sql.refresh(friendship);
	}

	public void save(List<Friendship> friendships) {
		friendships.forEach(f -> save(f));
	}

	public void save(Friendship friendship) {
		sql.merge(friendship);
	}

	public void delete(Friendship friendship) {
		sql.remove(friendship);
	}
}
