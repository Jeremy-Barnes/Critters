package com.critters.dal.dto;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
}
