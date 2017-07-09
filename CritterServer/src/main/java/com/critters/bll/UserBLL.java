package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.InventoryGrouping;
import com.critters.dal.dto.entity.*;
import com.lambdaworks.codec.Base64;
import com.lambdaworks.crypto.SCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.resource.spi.InvalidPropertyException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Jeremy on 8/9/2016.
 */
public class UserBLL {

	static final Logger logger = LoggerFactory.getLogger("application");


	public static List<User> searchUsers(String searchString){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		List<User> users = entityManager
				.createQuery("from User where firstName like :searchTerm or lastName like :searchTerm or userName like :searchTerm or emailAddress like :searchTerm and isActive = true")
				.setParameter("searchTerm", '%' + searchString + '%')
				.getResultList();
		entityManager.close();
		for(User user : users) {
			wipeSensitiveFields(user);
		}
		return users;
	}

	public static String createUserReturnUnHashedValidator(User user) throws UnsupportedEncodingException {
		user.setCritterbuxx(500); //TODO: economics
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
			throw e;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static User getUser(String selector, String validator) {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			User user = (User) entityManager.createQuery("from User where tokenSelector = :selector and isActive = true").setParameter("selector", selector).getSingleResult();

			if (verifyValidator(validator, user)) {
				user.initializeCollections();
				return user;
			} else {
				return null;
			}
		} catch (PersistenceException ex) {
			logger.debug("Login failed for selector " + selector + "and validator " + validator, ex);
			return null;
		} finally {
			entityManager.close();
		}
	}

	public static User getUser(String email, String password, boolean login) {
		logger.debug("Logging in with Email: " + email + " Password: " + password);
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		try {
			User user = (User) entityManager.createQuery("from User where emailAddress = :email and isActive = true").setParameter("email", email).getSingleResult();

			user.initializeCollections();
			if (login) {
				entityManager.getTransaction().begin();
				String validator = null;
				if (checkLogin(user.getPassword(), password, user.getSalt())) {
					validator = createSelectorAndHashValidator(user);
					entityManager.getTransaction().commit();
				} else {
					return null;
				}
				if (validator != null) user.setTokenValidator(validator);
			} else {
				user = wipeSensitiveFields(user);
			}
			return user;
		} catch(NoResultException nrex) {//no user found
			logger.error("Login failed for " + email, nrex);
			return null;
		} catch (Exception ex) {
			logger.error("A weird error occurred when logging in " + email, ex);
			return null;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static User getUser(int id) {
			User user = wipeSensitiveFields(getFullUser(id));
			return user;
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

	public static User updateUser(User changeUser, User sessionUser, UserImageOption imageOption) throws UnsupportedEncodingException, InvalidPropertyException {
		if(imageOption != null){ //WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never
			//from User Input. That way porn lies.
			imageOption = getUserImageOption(imageOption.getUserImageOptionID());
			sessionUser.setUserImagePath(imageOption.getImagePath());
		} else {
			sessionUser.setUserImagePath(null);
			changeUser.setUserImagePath(null);
		}

		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			entityManager.getTransaction().begin();

			if (changeUser.getPassword() != null && !changeUser.getPassword().isEmpty() && !changeUser.getPassword().equals(sessionUser.getPassword())) {
				hashAndSaltPassword(changeUser);
				sessionUser.setPassword(changeUser.getPassword());
				sessionUser.setSalt(changeUser.getSalt());
			}
			sessionUser.setFirstName(changeUser.getFirstName());
			sessionUser.setLastName(changeUser.getLastName());
			sessionUser.setPostcode(changeUser.getPostcode());
			if (changeUser.getEmailAddress() != null && changeUser.getEmailAddress().length() >= 5 && changeUser.getEmailAddress().contains("@")) {
				sessionUser.setEmailAddress(changeUser.getEmailAddress());
			} else if (changeUser.getEmailAddress() != null && !changeUser.getEmailAddress().isEmpty()) {
				throw new InvalidPropertyException("An invalid email address was supplied, please enter a valid email address. No account changes were made.");
			}
			sessionUser.setCity(changeUser.getCity());
			sessionUser.setState(changeUser.getState());
			sessionUser.setCountry(changeUser.getCountry());
			sessionUser.setIsActive(changeUser.getIsActive());
			sessionUser.setBirthDay(changeUser.getBirthDay());
			sessionUser.setBirthMonth(changeUser.getBirthMonth());
			entityManager.merge(sessionUser);
			entityManager.getTransaction().commit();
			return changeUser;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static void deleteUser(User user) throws InvalidPropertyException {
		try {
			user.setIsActive(false);
			updateUser(user, user, null);
			for (Pet pet : user.getPets()) {
				PetBLL.abandonPet(pet);
			}
		} catch (Exception e){
			logger.error("Delete user failed id:" + user.getUserID() + " email: " + user.getEmailAddress(), e);
		}
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

		verifyUserInventoryIsLoaded(user);
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

	protected static void verifyUserInventoryIsLoaded(User user){
		user.initializeInventory();
	}

	public static UserImageOption getUserImageOption(int id){ //todo caching
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			UserImageOption image = (UserImageOption) entityManager.createQuery("from UserImageOption where userImageOptionID = :id").setParameter("id", id).getSingleResult();
			return image;
		} catch (PersistenceException ex) {
			logger.debug("No such image option found with id " + id, ex);
			return null; //no image found
		} finally {
			entityManager.close();
		}
	}

	public static UserImageOption[] getUserImageOptions(){ //todo caching
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			UserImageOption[] images = (UserImageOption[]) entityManager.createQuery("from UserImageOption").getResultList().toArray(new UserImageOption[0]);
			return images;
		} catch (PersistenceException ex) {
			logger.debug("No image options found", ex);
			return null; //no images found
		} finally {
			entityManager.close();
		}
	}

	/***************** SECURITY STUFF **********************/
	private static void hashAndSaltPassword(User user) throws UnsupportedEncodingException {
		try {
			byte[] saltByte = new byte[16];
			SecureRandom.getInstance("SHA1PRNG").nextBytes(saltByte);
			String saltStr = new String(Base64.encode(saltByte));

			byte[] hashByte = SCrypt.scrypt(user.getPassword().getBytes("UTF-8"), saltStr.getBytes("UTF-8"), 16384, 8, 1, 64);
			String hashStr = new String(Base64.encode(hashByte));

			user.setPassword(hashStr);
			user.setSalt(saltStr);
		} catch (GeneralSecurityException ex) {
			logger.error("Could not run scrypt!", ex);
			System.exit(1); //if no secure algorithm is available, the service needs to shut down for emergency maintenance.
		}
	}

	private static String createSelectorAndHashValidator(User user) {
		String validatorStr = null;
		try {
			byte[] validatorByte = new byte[16];
			SecureRandom.getInstance("SHA1PRNG").nextBytes(validatorByte);
			validatorStr = new String(Base64.encode(validatorByte)); //Get in UTF
			byte[] hashedValidatorByte = SCrypt.scrypt(validatorStr.getBytes("UTF-8"), validatorStr.getBytes("UTF-8"), 16384, 8, 1, 64);

			user.setTokenSelector(UUID.randomUUID().toString());
			user.setTokenValidator(new String(Base64.encode(hashedValidatorByte)));
		} catch (GeneralSecurityException ex) {
			logger.error("Could not run scrypt!", ex);
			System.exit(1); //if no secure algorithm is available, the service needs to shut down for emergency maintenance.
		} catch (UnsupportedEncodingException ex) {
		} //shouldn't ever happen
		return validatorStr;
	}

	protected static boolean verifyValidator(String suppliedValidator, User dbUser) {
		String hashedCookieValidator = null;
		try {
			byte[] hashByte = SCrypt.scrypt(suppliedValidator.getBytes("UTF-8"), suppliedValidator.getBytes("UTF-8"), 16384, 8, 1, 64);
			hashedCookieValidator = new String(Base64.encode(hashByte));
		} catch (GeneralSecurityException ex) {
			logger.error("Could not run scrypt!", ex);
			System.exit(1); //if no secure algorithm is available, the service needs to shut down for emergency maintenance.
		} catch (UnsupportedEncodingException ex) {
			return false;
		}
		return hashedCookieValidator.equals(dbUser.getTokenValidator());
	}

	private static boolean checkLogin(String dbPasswordHash, String suppliedPassword, String suppliedSalt) {
		String hashStrConfirm = null;
		try {
			byte[] hashByte = SCrypt.scrypt(suppliedPassword.getBytes("UTF-8"), suppliedSalt.getBytes("UTF-8"), 16384, 8, 1, 64);
			hashStrConfirm = new String(Base64.encode(hashByte));
		} catch (GeneralSecurityException ex) {
			logger.error("Could not run scrypt!", ex);
			System.exit(1); //if no secure algorithm is available, the service needs to shut down for emergency maintenance.
		} catch (UnsupportedEncodingException ex) {
			return false;
		}
		return hashStrConfirm.equals(dbPasswordHash);
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
