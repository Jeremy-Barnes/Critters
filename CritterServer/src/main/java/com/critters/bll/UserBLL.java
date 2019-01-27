package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.critters.dal.accessors.DAL;
import com.critters.dto.InventoryGrouping;
import com.critters.dal.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jeremy on 8/9/2016.
 */
public class UserBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static List<User> searchForUser(String searchTerm) {
		List<User> users;
		try(DAL dal = new DAL()){
			users = dal.users.search(searchTerm);
		}

		for(User u : users){
			wipeSensitiveFields(u);
		}
		return users;
	}

	public static Integer alterUserCash(int userID, int amount) {
		try (DAL dal = new DAL()) {
			User user = dal.users.getUserByID(userID);
			user.setCritterbuxx(user.getCritterbuxx() + amount);
			dal.beginTransaction();
			dal.users.save(user);
			return dal.commitTransaction() ? user.getCritterbuxx() : null;
		}
	}

	public static String createUserReturnUnHashedValidator(User user) {
		user.setCritterbuxx(500); //TODO: economics and validation
		user.setIsActive(true);
		hashAndSaltPassword(user);
		String validatorUnHashed = createSelectorAndHashValidator(user);

		try(DAL dal = new DAL()) {
			user.setUserImagePath(dal.configuration.getUserImage(1).getImagePath());
			dal.beginTransaction();
			dal.users.save(user);
			if (dal.commitTransaction()) {
				return validatorUnHashed;
			} else {
				logger.error("User creation failed for user " + user.toString());
				return null;
			}
		}
	}

	public static User loginUser(String selector, String validator) {
		logger.debug("Logging in with selector: " + selector + " validator: " + validator);
		User user;
		try(DAL dal = new DAL()) {
			user = dal.users.getUserByTokenSelector(selector);
			if(user != null)
				user.initializeCollections();
		}
		return user != null && SecurityBLL.validateEncryptedMatch(validator, validator, user.getTokenValidator()) ? user : null;
	}

	public static User getUser(String email, String password, boolean login) {
		logger.debug("Logging in with Email: " + email + " Password: " + password);
		User user;
		try(DAL dal = new DAL()) {
			user = dal.users.getUserByEmailAddress(email);
			if(user == null) return null;
			String validator = createSelectorAndHashValidator(user);
			user.initializeCollections();
			if (login && SecurityBLL.validateEncryptedMatch(password, user.getSalt(), user.getPassword())) {
				dal.beginTransaction();
				dal.users.save(user);
				dal.commitTransaction();
				user.setTokenValidator(validator); //pass back unhashed validator to the user
			} else {
				user = login ? null : wipeSensitiveFields(user); //login attempt failed validate
				logger.info("Failed login attempt for email " + email);
			}
		}
		return user;
	}

	public static User getUserForDisplay(int id) {
		User user = null;
		try(DAL dal = new DAL()) {
			user = dal.users.getUserByID(id);
			if(user!= null)
				user.initializeCollections();
		}
		return wipeSensitiveFields(user);
	}

	public static User updateUser(User changeUser, User sessionUser, UserImageOption imageOption) {
		//todo validate
		if (!Extensions.isNullOrEmpty(changeUser.getPassword()) && !changeUser.getPassword().equals(sessionUser.getPassword())) {
			hashAndSaltPassword(changeUser);
			sessionUser.setPassword(changeUser.getPassword());
			sessionUser.setSalt(changeUser.getSalt());
		}
		sessionUser.setFirstName(changeUser.getFirstName());
		sessionUser.setLastName(changeUser.getLastName());
		sessionUser.setPostcode(changeUser.getPostcode());
		if(!Extensions.isNullOrEmpty(changeUser.getEmailAddress())) //TODO MORE VALIDATION
			sessionUser.setEmailAddress(changeUser.getEmailAddress());
		sessionUser.setCity(changeUser.getCity());
		sessionUser.setState(changeUser.getState());
		sessionUser.setCountry(changeUser.getCountry());
		sessionUser.setIsActive(changeUser.getIsActive());
		sessionUser.setBirthDay(changeUser.getBirthDay());
		sessionUser.setBirthMonth(changeUser.getBirthMonth());

		try(DAL dal = new DAL()){
			if(imageOption != null){ //WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never from User Input. That way porn lies.
				imageOption = getUserImageOption(imageOption.getUserImageOptionID());
				sessionUser.setUserImagePath(imageOption.getImagePath());
			} else {
				sessionUser.setUserImagePath(null);
			}

			dal.beginTransaction();
			dal.users.save(sessionUser);
			if(!dal.commitTransaction()) {
				logger.error("Failed to update a user", changeUser);
				changeUser = null;
			}
		}
		return changeUser;
	}

	public static boolean deleteUser(int userID) {
		try(DAL dal = new DAL()) {
			User user = dal.users.getUserByID(userID);
			user.initializeCollections();
			user.setIsActive(false);
			for (Pet pet : user.getPets()) {
				pet.setIsAbandoned(true);
			}
			dal.beginTransaction();
			dal.users.save(user);
			dal.pets.save(user.getPets());
			if(!dal.commitTransaction()) {
				logger.error("Delete user failed id:" + user.getUserID() + " email: " + user.getEmailAddress());
				return false;
			}
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

	public static boolean isUserNameTaken(String userName){
		try(DAL dal = new DAL()){
			return dal.users.isUserNameInUse(userName);
		}
	}

	public static boolean isEmailAddressTaken(String email){
		try(DAL dal = new DAL()){
			return dal.users.isUserNameInUse(email);
		}
	}

	public static List<InventoryGrouping> getInventory(User user){
		return CommerceBLL.groupItems(getUserInventory(user.getUserID()));
	}

	public static List<Item> getUserInventory(int userID){
		try(DAL dal = new DAL()){
			return dal.items.getItemsByUserOwnerID(userID);
		}
	}

	public static boolean giveOrDiscardInventoryItems(Item[] dropItems, User user, Integer recipientID){
		try(DAL dal = new DAL()){
			List<Item> dbDropItems = dal.items.getItems(dropItems);
			List<Integer> ids = dbDropItems.stream().map(Item::getInventoryItemId).collect(Collectors.toList());
			if (!dbDropItems.stream().allMatch(i -> i.getOwnerId() == user.getUserID())) {
				return false;
			}
			user.initializeInventory();
			List<Item> userOwnedItems = user.getInventory().stream().filter(i -> ids.contains(i.getInventoryItemId())).collect(Collectors.toList());
			if(userOwnedItems != null && userOwnedItems.size() == dbDropItems.size()){
				for(int i = 0; i < userOwnedItems.size(); i++) {
					user.getInventory().remove(userOwnedItems.get(i));
					userOwnedItems.get(i).setOwnerId(recipientID);
					userOwnedItems.get(i).setPrice(null);
				}
				dal.beginTransaction();
				dal.items.save(userOwnedItems);
				dal.users.save(user);
				dal.commitTransaction();
				return true;
			}
		}
		return false;
	}

	public static UserImageOption getUserImageOption(int id) { //todo caching
		UserImageOption image = null;
		try (DAL dal = new DAL()) {
			image = dal.configuration.getUserImage(id);
		}
		return image;
	}

	public static UserImageOption[] getUserImageOptions() {
		List<UserImageOption> images = null;
		try (DAL dal = new DAL()) {
			images = dal.configuration.getUserImages();
		}
		return images.toArray(new UserImageOption[0]);
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

}
