package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Jeremy on 3/2/2017.
 */
public class StoreClerkImageOptions {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int storeClerkImageOptionID;
	private String imagePath;

	public StoreClerkImageOptions(int storeClerkImageOptionID, String imagePath) {
		this.storeClerkImageOptionID = storeClerkImageOptionID;
		this.imagePath = imagePath;
	}

	public StoreClerkImageOptions() {

	}

	public int getStoreClerkImageOptionID() {
		return storeClerkImageOptionID;
	}

	public void setStoreClerkImageOptionID(int storeClerkImageOptionID) {
		this.storeClerkImageOptionID = storeClerkImageOptionID;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
