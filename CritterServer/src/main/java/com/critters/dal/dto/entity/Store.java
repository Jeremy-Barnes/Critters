package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Jeremy on 12/4/2016.
 */
@Entity
@Table(name="storeConfigs")
public class Store {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int storeConfigID;

	private int ownerId;
	private String description;
	private String name;


	public Store(int storeConfigID, int ownerId, String name, String description) {
		this.storeConfigID = storeConfigID;
		this.ownerId = ownerId;
		this.name = name;
		this.description = description;
	}

	public Store() {}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getInventoryItemId() {
		return storeConfigID;
	}

	public void setInventoryItemId(int storeConfigID) {
		this.storeConfigID = storeConfigID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
