package com.critters.dal;

import com.critters.dal.dto.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class UserAccessor  {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	UserAccessor(EntityManager hibernateHelper){
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

	public User getUserByEmailAddress(String email){
		User user = null;
		try {
			user = (User) sql.createQuery("from User where emailAddress = :email and isActive = true").setParameter("email", email).getSingleResult();
		} catch(PersistenceException ex) {
			logger.error("User lookup failed for " + email, ex);
		}
		return user;
	}

	public List<User> search(String searchTerm){
		StoredProcedureQuery query = sql.createStoredProcedureQuery("usersearch", User.class);
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.setParameter(1, "%" + searchTerm + "%");
		query.execute();
		return query.getResultList();
	}

	public boolean isUserNameInUse(String userName){
		boolean valid = true;
		valid = (userName != null && !userName.isEmpty());
		if(valid) {
			valid = !(boolean) sql.createNativeQuery("SELECT EXISTS(SELECT 1 from users where userName = ?1)")
											.setParameter(1, userName)
											.getSingleResult();
		}

		return valid;
	}

	public boolean isEmailAddressInUse(String email){
		boolean valid = true;
		valid = (email != null && !email.isEmpty());
		if(valid) {
			valid = !(boolean) sql.createNativeQuery("SELECT EXISTS(SELECT 1 from users where emailAddress = ?1)")
											.setParameter(1, email)
											.getSingleResult();
		}
		return valid;
	}


	public void save(List<User> users) {
		users.forEach(u -> save(u));
	}

	public void save(User user) {
		sql.merge(user);
	}

}
