package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jeremy on 3/2/2017.
 */
@Entity
@Table(name="storeClerkImageOptions")
public class StoreClerkImageOption extends DTO {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int storeClerkImageOptionID;
	private String imagePath;

	public StoreClerkImageOption(int storeClerkImageOptionID, String imagePath) {
		this.storeClerkImageOptionID = storeClerkImageOptionID;
		this.imagePath = imagePath;
	}

	public StoreClerkImageOption() {

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
