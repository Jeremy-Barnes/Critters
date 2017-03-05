package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Jeremy on 3/2/2017.
 */
public class StoreBackgroundImageOption {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int storeBackgroundImageOptionID;
	private String imagePath;

	public StoreBackgroundImageOption(int storeBackgroundImageOptionID, String imagePath) {
		this.storeBackgroundImageOptionID = storeBackgroundImageOptionID;
		this.imagePath = imagePath;
	}

	public StoreBackgroundImageOption() {

	}

	public int getStoreBackgroundImageOptionID() {
		return storeBackgroundImageOptionID;
	}

	public void setStoreBackgroundImageOptionID(int storeBackgroundImageOptionID) {
		this.storeBackgroundImageOptionID = storeBackgroundImageOptionID;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


}
