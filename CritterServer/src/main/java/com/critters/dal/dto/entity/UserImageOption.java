package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Jeremy on 3/2/2017.
 */
public class UserImageOption {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int userImageOptionID;
	private String imagePath;

	public UserImageOption(int userImageOptionID, String imagePath) {
		this.userImageOptionID = userImageOptionID;
		this.imagePath = imagePath;
	}

	public UserImageOption() {

	}

	public int getUserImageOptionID() {
		return userImageOptionID;
	}

	public void setUserImageOptionID(int userImageOptionID) {
		this.userImageOptionID = userImageOptionID;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
