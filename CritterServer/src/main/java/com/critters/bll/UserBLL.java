package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.InventoryGrouping;
import com.critters.dal.dto.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jeremy on 8/9/2016.
 */
public class UserBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static List<User> searchUsers(String searchString){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		List<User> users = entityManager
				.createQuery("from User where firstName like :searchTerm " +
									 "or lastName like :searchTerm or userName like :searchTerm " +
									 "or emailAddress like :searchTerm " +
									 "and isActive = true")
				.setParameter("searchTerm", '%' + searchString + '%')
				.getResultList();
		entityManager.close();
		for(User user : users) {
			wipeSensitiveFields(user);
		}
		return users;
	}

	public static List<User> searchForUser(String searchTerm) {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("usersearch",User.class);

		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
		query.setParameter(1, "%" + searchTerm + "%");
		query.execute();
		List<User> results = query.getResultList();

		entityManager.close();
		for(User u : results){
			wipeSensitiveFields(u);
		}
		return results;
	}

	public static void giveUserCash(User user, int amount) {
		
	}


	public static String createUserReturnUnHashedValidator(User user) {
		user.setCritterbuxx(500); //TODO: economics and validation
		user.setIsActive(true);
		user.setUserImagePath(getUserImageOption(1).getImagePath());
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			hashAndSaltPassword(user);
			String validatorUnHashed = createSelectorAndHashValidator(user);
			entityManager.persist(user);
			entityManager.getTransaction().commit();
			return validatorUnHashed;
		} catch(Exception e) {
			logger.error("User creation failed for user " + user.toString(), e);
			return null;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static User getUser(String selector, String validator) {
		logger.debug("Logging in with selector: " + selector + " validator: " + validator);
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		User user = null;
		try {
			user = (User) entityManager.createQuery("from User where tokenSelector = :selector and isActive = true").setParameter("selector", selector).getSingleResult();
			user.initializeCollections();
		} catch (PersistenceException ex) {
			logger.debug("No selector " + selector + " found", ex);
		} finally {
			entityManager.close();
		}
		return user != null && SecurityBLL.validateEncryptedMatch(validator, validator, user.getTokenValidator()) ? user : null;
	}

	public static User getUser(String email, String password, boolean login) {
		logger.debug("Logging in with Email: " + email + " Password: " + password);
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		User user = null;
		try {
			user = (User) entityManager.createQuery("from User where emailAddress = :email and isActive = true").setParameter("email", email).getSingleResult();
			user.initializeCollections();
			if (login && SecurityBLL.validateEncryptedMatch(password, user.getSalt(), user.getPassword())) {
				entityManager.getTransaction().begin();
				String validator = createSelectorAndHashValidator(user);
				entityManager.getTransaction().commit();
				user.setTokenValidator(validator); //pass back unhashed validator to the user
			} else {
				user = login ? null : wipeSensitiveFields(user); //login attempt failed validate
				logger.info("Failed login attempt for email " + email);
			}
		} catch(NoResultException nrex) {//no user found
			logger.error("User lookup failed for " + email, nrex);
		} catch (Exception ex) {
			logger.error("A weird error occurred when logging in " + email, ex);
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
		return user;
	}

	public static User getUser(int id) {
			User user = wipeSensitiveFields(getFullUser(id));
			return user;
	}

	public static User updateUser(User changeUser, User sessionUser, UserImageOption imageOption) {
		//todo validate
		if(imageOption != null){ //WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never from User Input. That way porn lies.
			imageOption = getUserImageOption(imageOption.getUserImageOptionID());
			sessionUser.setUserImagePath(imageOption.getImagePath());
		} else {
			sessionUser.setUserImagePath(null);
			changeUser.setUserImagePath(null);
		}

		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			entityManager.getTransaction().begin();

			if (!Extensions.isNullOrEmpty(changeUser.getPassword()) && !changeUser.getPassword().equals(sessionUser.getPassword())) {
				hashAndSaltPassword(changeUser);
				sessionUser.setPassword(changeUser.getPassword());
				sessionUser.setSalt(changeUser.getSalt());
			}
			sessionUser.setFirstName(changeUser.getFirstName());
			sessionUser.setLastName(changeUser.getLastName());
			sessionUser.setPostcode(changeUser.getPostcode());
			sessionUser.setEmailAddress(changeUser.getEmailAddress());
			sessionUser.setCity(changeUser.getCity());
			sessionUser.setState(changeUser.getState());
			sessionUser.setCountry(changeUser.getCountry());
			sessionUser.setIsActive(changeUser.getIsActive());
			sessionUser.setBirthDay(changeUser.getBirthDay());
			sessionUser.setBirthMonth(changeUser.getBirthMonth());
			entityManager.merge(sessionUser);
			entityManager.getTransaction().commit();
			return changeUser;
		}  finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
				logger.error("Failed to update a user", changeUser);
			}
			entityManager.close();
		}
	}

	public static boolean deleteUser(User user) {
		try {
			user.setIsActive(false);
			updateUser(user, user, null);
			for (Pet pet : user.getPets()) {
				PetBLL.abandonPet(pet);
			}
		} catch (Exception e){
			logger.error("Delete user failed id:" + user.getUserID() + " email: " + user.getEmailAddress(), e);
			return false;
		}
		return true;
	}

	public static User wipeSensitiveFields(User user){ return wipeSensitiveFields(user, false);}

	public static User wipeSensitiveFields(User user, boolean ignoreMoney) {
		user.setSalt("");
		user.setPassword("");
		user.setTokenSelector("");
		user.setTokenValidator("");
		user.setEmailAddress("");
		if(!ignoreMoney) user.setCritterbuxx(0);
		user.setInventory(null);
		if(user.getFriends()!= null)
			for(Friendship friend: user.getFriends()){
				wipeSensitiveFields(friend.getRequested());
				wipeSensitiveFields(friend.getRequester());
			}
		return user;
	}

	public static boolean isUserNameValid(String userName){

		boolean valid = true;
		valid = (userName != null && !userName.isEmpty()); //todo: content filter
		if(valid) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			valid = !(boolean) entityManager.createNativeQuery("SELECT EXISTS(SELECT 1 from users where userName = ?1)")
											.setParameter(1, userName)
											.getSingleResult();
			entityManager.close();
		}

		return valid;
	}

	public static boolean isEmailAddressValid(String email){
		boolean valid = true;
		valid = (email != null && !email.isEmpty()); //todo: content filter
		if(valid) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			valid = !(boolean) entityManager.createNativeQuery("SELECT EXISTS(SELECT 1 from users where emailAddress = ?1)")
											.setParameter(1, email)
											.getSingleResult();
			entityManager.close();
		}
		return valid;
	}

	public static List<InventoryGrouping> getInventory(User user){
		return CommerceBLL.groupItems(getUserInventory(user));
	}

	public static List<Item> getUserInventory(User user){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		List<Item> inventory = entityManager
				.createQuery("from Item where ownerId = :id")
				.setParameter("id", user.getUserID())
				.getResultList();
		entityManager.close();
		return inventory;
	}

	public static void discardInventoryItems(Item[] items, User user){
		List<Item> streamableItems = Arrays.asList(items);
		List<Integer> ids = streamableItems.stream().map(Item::getInventoryItemId).collect(Collectors.toList());

		user.initializeInventory();
		Item[] resultant = user.getInventory().stream().filter(i -> ids.contains(i.getInventoryItemId())).toArray(Item[]::new);
		if(resultant != null && resultant.length == items.length) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				entityManager.getTransaction().begin();
				for(int i = 0; i < resultant.length; i++) {
					user.getInventory().remove(resultant[i]);
					resultant[i].setOwnerId(null);
					resultant[i].setPrice(null);
					entityManager.merge(resultant[i]);
				}
				entityManager.merge(user);
				entityManager.getTransaction().commit();
			} finally {
				if(entityManager.getTransaction().isActive()){
					entityManager.getTransaction().rollback();
				}
				entityManager.close();
			}
		}
	}

	public static UserImageOption getUserImageOption(int id) { //todo caching
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			UserImageOption image = (UserImageOption) entityManager.createQuery("from UserImageOption where userImageOptionID = :id").setParameter("id", id)
																   .getSingleResult();
			return image;
		} catch (NoResultException nrex) {
			logger.info("No such image option found with id " + id, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for id " + id, ex);
		} finally {
			entityManager.close();
		}
		return null;
	}

	public static UserImageOption[] getUserImageOptions() { //todo caching
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		UserImageOption[] images = null;
		try {
		 	images = (UserImageOption[]) entityManager.createQuery("from UserImageOption").getResultList().toArray(new UserImageOption[0]);
		}
		catch (PersistenceException ex) {
			logger.error("Database error searching for images!", ex);
		}  finally {
			entityManager.close();
		}
		return images;
	}

	/***************** SECURITY STUFF **********************/
	private static void hashAndSaltPassword(User user) {
			String salt = SecurityBLL.getRandomString(16);
			String hashword = SecurityBLL.hashAndSaltEncrypt(user.getPassword(), salt);
			user.setPassword(hashword);
			user.setSalt(salt);
	}

	private static String createSelectorAndHashValidator(User user) {
		String validator = SecurityBLL.getRandomString(16);
		user.setTokenSelector(SecurityBLL.getGUID());
		user.setTokenValidator(SecurityBLL.hashAndSaltEncrypt(validator, validator));
		return validator;
	}

	protected static User getFullUser(int id){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			User user = (User) entityManager.createQuery("from User where userID = :id and isActive = true").setParameter("id", id).getSingleResult();
			return user;
		} catch (PersistenceException ex) {
			logger.debug("No active user found with id " + id, ex);
			return null; //no user found
		} finally {
			entityManager.close();
		}
	}
}
