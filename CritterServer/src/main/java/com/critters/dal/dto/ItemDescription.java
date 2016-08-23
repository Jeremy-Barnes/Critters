package com.critters.dal.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Entity
@Table(name="itemconfigs")
public class ItemDescription {

	public ItemDescription(){}

	public ItemDescription(int itemconfigid, String itemName, String itemDescription) {
		this.itemconfigid = itemconfigid;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
	}

	public int getItemconfigid() {
		return itemconfigid;
	}

	public void setItemconfigid(int itemconfigid) {
		this.itemconfigid = itemconfigid;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	@Id
	int itemconfigid;
	String itemName;
	String itemDescription;

}
