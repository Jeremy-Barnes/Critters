package com.critters.dal;

import com.critters.dal.dto.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class UserAccessor  {
	static final Logger logger = LoggerFactory.getLogger("application");
	HibernateUtil.HibernateHelper sql;

	public UserAccessor(HibernateUtil.HibernateHelper hibernateHelper){
		sql = hibernateHelper;
	}

	public User getUserByID(int id){
		User user = null;
		try {
			user = (User) sql.createQuery("from User where userID = :id and isActive = true").setParameter("id", id).getSingleResult();
		} catch (PersistenceException ex) {
			logger.debug("No active user found with id " + id, ex);
		}
		return user;
	}

	public User getUserByTokenSelector(String selector) {
		User user = null;
		try {
			user = (User) sql.createQuery("from User where tokenSelector = :selector and isActive = true").setParameter("selector", selector).getSingleResult();
		} catch(PersistenceException ex) {
			logger.debug("No selector " + selector + " found", ex);
		}
		return user;
	}

	public void getUserByUsername() {

	}

	public User getUserByEmailAddress(String email){
		User user = null;
		try {
			user = (User) sql.createQuery("from User where emailAddress = :email and isActive = true").setParameter("email", email).getSingleResult();
		} catch(PersistenceException ex) {
			logger.error("User lookup failed for " + email, ex);
		}
		return user;
	}

	public void save(List<User> users) {
		users.forEach(u -> save(u));
	}

	public void save(User user) {
		sql.entityManager.merge(user);
	}

}
