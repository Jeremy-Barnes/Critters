package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


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

	private Integer ownerId;
	private String description;
	private String name;

	@Transient
	private List<Item> storeStock;

	public Store(int storeConfigID, Integer ownerId, String name, String description) {
		this.storeConfigID = storeConfigID;
		this.ownerId = ownerId;
		this.name = name;
		this.description = description;
	}

	public Store() {}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public int getStoreConfigID() {
		return storeConfigID;
	}

	public void setStoreConfigID(int storeConfigID) {
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

	public List<Item> getStoreStock() {
		return storeStock;
	}

	public void setStoreStock(List<Item> stock) {
		this.storeStock = stock;
	}

}
