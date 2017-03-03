package com.critters.dal.dto.entity;

import com.critters.bll.UserBLL;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.collection.internal.PersistentBag;

import javax.mail.Store;
import javax.persistence.*;
import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jeremy on 8/9/2016.
 */
@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int userID;
	private String userName;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String password;
	private String sex;
	private int birthDay;
	private int birthMonth;
	private String salt;
	private String city;
	private String state;
	private String country;
	private String postcode;
	private String tokenSelector;
	private String tokenValidator;
	private int critterbuxx;
	private boolean isActive;
	private String userImagePath;


	@OneToMany
	@JoinColumn(name="requesteruserid")
	private List<Friendship> friends;

	@OneToMany
	@JoinColumn(name="requesteduserid")
	private List<Friendship> friendsOf;

	@OneToMany
	@JoinColumn(name="ownerid")
	private List<Item> inventory;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="ownerid")
	private List<Pet> pets;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="ownerid")
	private List<com.critters.dal.dto.entity.Store> store;

	public User(int userID, String userName, String firstName, String lastName, String emailAddress, String password, String sex, int birthMonth, int birthDay,
				String salt, String city, String state, String country, String postcode, String tokenSelector, String tokenValidator,
				int critterbuxx, List<Friendship> friends, List<Friendship> friendsOf, boolean isActive, List<com.critters.dal.dto.entity.Store> store, String userImagePath) {
		this.userID = userID;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.sex = sex;
		this.birthMonth = birthMonth;
		this.birthDay = birthDay;
		this.salt = salt;
		this.city = city;
		this.state = state;
		this.country = country;
		this.postcode = postcode;
		this.tokenSelector = tokenSelector;
		this.tokenValidator = tokenValidator;
		this.critterbuxx = critterbuxx;
		this.friends = friends;
		this.friendsOf = friendsOf;
		this.isActive = isActive;
		this.store = store;
		this.userImagePath = userImagePath;
	}

	public User(User copyUser) {
		this.userID = copyUser.userID;
		this.userName = copyUser.userName;
		this.firstName = copyUser.firstName;
		this.lastName = copyUser.lastName;
		this.emailAddress = copyUser.emailAddress;//todo should this really be copied?
		this.password = copyUser.password;//todo should this really be copied?
		this.sex = copyUser.sex;
		this.birthdate = copyUser.birthdate;
		this.salt = copyUser.salt;//todo should this really be copied?
		this.city = copyUser.city;
		this.state = copyUser.state;
		this.country = copyUser.country;
		this.postcode = copyUser.postcode;
		this.tokenSelector = copyUser.tokenSelector; //todo should this really be copied?
		this.tokenValidator = copyUser.tokenValidator;//todo should this really be copied?
		this.critterbuxx = copyUser.critterbuxx;
		this.isActive = copyUser.isActive;
		this.friends = null;
		this.friendsOf = null;
		this.userImagePath = copyUser.userImagePath;
	}

	public User(){}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonth(int birthMonth) {
		this.birthMonth = birthMonth;
	}
	
	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(int birthDay) {
		this.birthDay = birthDay;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {userImagePath
		this.postcode = postcode;
	}

	public String getTokenSelector() {
		return tokenSelector;
	}

	public void setTokenSelector(String tokenSelector) {
		this.tokenSelector = tokenSelector;
	}

	public String getTokenValidator() {
		return tokenValidator;
	}

	public void setTokenValidator(String tokenValidator) {
		this.tokenValidator = tokenValidator;
	}

	public int getCritterbuxx() {
		return critterbuxx;
	}

	public void setCritterbuxx(int critterbuxx) {
		this.critterbuxx = critterbuxx;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public String getUserImagePath() {
		return userImagePath;
	}

	public void setUserImagePath(String userImagePath) {
		this.userImagePath = userImagePath;
	}


	public List<Friendship> getFriends() {
		List<Friendship> frnds = new ArrayList();
		if(friends != null && ((friends instanceof PersistentBag) ? ((PersistentBag)friends).wasInitialized() : true)) {
			for (Friendship friendship : friends) {
				friendship.setRequester(new User(friendship.getRequester()));
				friendship.setRequested(new User(friendship.getRequested()));
			}
			frnds.addAll(friends);
		}
		else if(friends != null &&  !(friends instanceof PersistentBag))  {
			frnds.addAll(friends);
		}

		if(friendsOf != null && ((friendsOf instanceof PersistentBag) ? ((PersistentBag)friendsOf).wasInitialized() : true)) {
			for (Friendship friendship : friendsOf) {
				friendship.setRequested(new User(friendship.getRequested()));
				friendship.setRequester(new User(friendship.getRequester()));
			}
			frnds.addAll(friendsOf);
		} else if(friendsOf != null && !(friendsOf instanceof PersistentBag)) {
			frnds.addAll(friendsOf);
		}

		return frnds;
	}

	public List<Pet> getPets() {
		if((pets instanceof PersistentBag && ((PersistentBag)pets).wasInitialized()) || pets instanceof ArrayList) {
			return this.pets;
		} else {
			return null;
		}
	}

	public void setPets(List<Pet> zoo) {
		this.pets = zoo;
	}

	public List<Item> getInventory() {
		if((inventory instanceof PersistentBag && ((PersistentBag)inventory).wasInitialized()) || inventory instanceof ArrayList) {
			return inventory;
		}
		return null;
	}

	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}

	public void setFriends(List<Friendship> friendships) {
		this.friends = friendships;
	}

	public void setFriendsOf(List<Friendship> friendships) {
		this.friendsOf = friendships;
	}

	public void initializeCollections() {
		Hibernate.initialize(friends);
		Hibernate.initialize(friendsOf);
		Hibernate.initialize(pets);
		Hibernate.initialize(store);
		Hibernate.initialize(inventory);
	}


  	public void initializeInventory(){
		this.setInventory(UserBLL.getUserInventory(this));
	}


	public List<com.critters.dal.dto.entity.Store> getStore(){
		if((inventory instanceof PersistentBag && ((PersistentBag)inventory).wasInitialized()) || inventory instanceof ArrayList) {
			return this.store;
		} else {
			return null;
		}
	}

	public void setStore(List<com.critters.dal.dto.entity.Store> store){
		this.store = store;
	}

	public void nullAllButID(){
		this.userName = null;
		this.firstName = null;
		this.lastName = null;
		this.emailAddress = null;
		this.password = null;
		this.sex = null;
		this.birthdate = null;
		this.salt = null;
		this.city = null;
		this.state = null;
		this.country = null;
		this.postcode = null;
		this.tokenSelector = null;
		this.tokenValidator = null;
		this.critterbuxx = -1;
		this.isActive = true;
		this.friends = null;
		this.friendsOf = null;
		this.inventory = null;
		this.pets = null;
		this.store = null;
	}
}
