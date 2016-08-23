package com.critters.dal.dto;

import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.collection.internal.PersistentBag;

import javax.persistence.*;
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
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	private String salt;
	private String city;
	private String state;
	private String country;
	private String postcode;
	private String tokenSelector;
	private String tokenValidator;
	private int critterbuxx;

	@OneToMany
	@JoinColumn(name="requesteruserid")
	private List<Friendship> friends;
	@OneToMany
	@JoinColumn(name="requesteduserid")
	private List<Friendship> friendsOf;



	@OneToMany
	@JoinColumn(name="ownerid")
	private List<Item> inventory;


	public User(int userID, String userName, String firstName, String lastName, String emailAddress, String password, Date birthdate,
				String salt, String city, String state, String country, String postcode, String tokenSelector, String tokenValidator,
				int critterbuxx, List<Friendship> friends, List<Friendship> friendsOf) {
		this.userID = userID;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.birthdate = birthdate;
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

	}

	public User(User copyUser) {
		this.userID = copyUser.userID;
		this.userName = copyUser.userName;
		this.firstName = copyUser.firstName;
		this.lastName = copyUser.lastName;
		this.emailAddress = copyUser.emailAddress;
		this.birthdate = copyUser.birthdate;
		this.city = copyUser.city;
		this.state =copyUser.state;
		this.country = copyUser.country;
		this.postcode = copyUser.postcode;
		this.critterbuxx = copyUser.critterbuxx;
		this.friends = null;
		this.friendsOf = null;
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public void setPostcode(String postcode) {
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

	public List<Friendship> getFriends() {
		List<Friendship> frnds = new ArrayList();
		if(friends != null && ((friends instanceof PersistentBag) && ((PersistentBag)friends).wasInitialized())) {
			for (Friendship friendship : friends) {
				friendship.setRequester(new User(this));
				friendship.setRequested(new User(friendship.getRequested()));
			}
			frnds.addAll(friends);
		} else if(friends != null) {
			frnds.addAll(friends);
		}
		if(friendsOf != null && ((friendsOf instanceof PersistentBag) && ((PersistentBag)friendsOf).wasInitialized())) {
			for (Friendship friendship : friendsOf) {
				friendship.setRequested(new User(this));
				friendship.setRequester(new User(friendship.getRequester()));
			}
			frnds.addAll(friendsOf);
		} else if(friendsOf != null) {
			frnds.addAll(friendsOf);
		}

		return frnds;
	}

	public List<Item> getInventory() {
		return inventory;
	}

	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}

	public void setFriends(List<Friendship> friendships) {
		this.friends = friendships;
//		if(friends != null)
//			for(Friendship friendship: friends) {
//				friendship.setRequester(new User(this));
//				friendship.setRequested(new User(friendship.getRequested()));
//			}
	}

	public void setFriendsOf(List<Friendship> friendships) {
		this.friendsOf = friendships;
//		if(friendsOf != null)
//			for(Friendship friendship: friendsOf) {
//				friendship.setRequested(new User(this));
//				friendship.setRequester(new User(friendship.getRequester()));
//			}
	}

	public void initializeCollections() {
		Hibernate.initialize(friends);
		Hibernate.initialize(friendsOf);
	}

}
