package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jeremy on 3/2/2017.
 */
@Entity
@Table(name="storeBackgroundImageOptions")
public class StoreBackgroundImageOption extends DTO {
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
