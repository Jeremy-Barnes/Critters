package com.critters.dal.dto.entity;

<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
=======
import com.critters.bll.UserBLL;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.collection.internal.PersistentBag;

import javax.mail.Store;
import javax.persistence.*;
<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
=======
import java.lang.reflect.Array;

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
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
	private boolean isActive;

	@OneToMany
	@JoinColumn(name="requesteruserid")
	private List<Friendship> friends;

	@OneToMany
	@JoinColumn(name="requesteduserid")
	private List<Friendship> friendsOf;

	@OneToMany
<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
	@JoinColumn(name="requesteruserid")
	private List<Friendship> friends;
	@OneToMany
	@JoinColumn(name="requesteduserid")
	private List<Friendship> friendsOf;

	@OneToMany
=======
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
	@JoinColumn(name="ownerid")
	private List<Item> inventory;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="ownerid")
<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
	private List<Pet> zoo;


	public User(int userID, String userName, String firstName, String lastName, String emailAddress, String password, Date birthdate,
				String salt, String city, String state, String country, String postcode, String tokenSelector, String tokenValidator,
				int critterbuxx, List<Friendship> friends, List<Friendship> friendsOf) {
=======
	private List<Pet> pets;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name="ownerid")
	private List<com.critters.dal.dto.entity.Store> store;

	public User(int userID, String userName, String firstName, String lastName, String emailAddress, String password, String sex, Date birthdate,
				String salt, String city, String state, String country, String postcode, String tokenSelector, String tokenValidator,
				int critterbuxx, List<Friendship> friends, List<Friendship> friendsOf, boolean isActive, List<com.critters.dal.dto.entity.Store> store) {
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
		this.userID = userID;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.sex = sex;
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
<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java

=======
		this.isActive = isActive;
		this.store = store;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
	}

	public User(User copyUser) {
		this.userID = copyUser.userID;
		this.userName = copyUser.userName;
		this.firstName = copyUser.firstName;
		this.lastName = copyUser.lastName;
		this.emailAddress = copyUser.emailAddress;
		this.password = copyUser.password;
		this.sex = copyUser.sex;
		this.birthdate = copyUser.birthdate;
		this.salt = copyUser.salt;
		this.city = copyUser.city;
		this.state = copyUser.state;
		this.country = copyUser.country;
		this.postcode = copyUser.postcode;
		this.tokenSelector = copyUser.tokenSelector;
		this.tokenValidator = copyUser.tokenValidator;
		this.critterbuxx = copyUser.critterbuxx;
<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
=======
		this.isActive = copyUser.isActive;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
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

<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
=======
	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
	public List<Friendship> getFriends() {
		List<Friendship> frnds = new ArrayList();
		if(friends != null && ((friends instanceof PersistentBag) ? ((PersistentBag)friends).wasInitialized() : true)) {
			for (Friendship friendship : friends) {
				friendship.setRequester(new User(this));
				friendship.setRequested(new User(friendship.getRequested()));
			}
			frnds.addAll(friends);
		} else if(friends != null && !((PersistentBag)friends).wasInitialized())  {
			return null;
		}
		if(friendsOf != null && ((friendsOf instanceof PersistentBag) ? ((PersistentBag)friendsOf).wasInitialized() : true)) {
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

<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
	public List<Pet> getZoo() {
		return zoo;
	}

	public void setZoo(List<Pet> zoo) {
		this.zoo = zoo;
	}

	public List<Item> getInventory() {
		if(inventory instanceof PersistentBag && ((PersistentBag)inventory).wasInitialized()) {
=======
	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> zoo) {
		this.pets = zoo;
	}

	public List<Item> getInventory() {
		if((inventory instanceof PersistentBag && ((PersistentBag)inventory).wasInitialized()) || inventory instanceof ArrayList) {
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
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
<<<<<<< HEAD:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
		Hibernate.initialize(zoo);
		Hibernate.initialize(inventory);
	}

=======
		Hibernate.initialize(pets);
		Hibernate.initialize(inventory);
	}

  public void initializeInventory(){
		this.setInventory(UserBLL.getInventory(this));
	}


	public List<com.critters.dal.dto.entity.Store> getStore(){
		return this.store;
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
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a:CritterServer/src/main/java/com/critters/dal/dto/entity/User.java
}
