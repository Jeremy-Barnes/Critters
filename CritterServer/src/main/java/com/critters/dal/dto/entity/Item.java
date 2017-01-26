package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Entity
@Table(name="inventoryitems")
public class Item {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int inventoryItemId;
<<<<<<< HEAD
	private int ownerId;
=======
	private Integer ownerId;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

	@ManyToOne
	@JoinColumn(name="itemTypeId", updatable = false)
	private ItemDescription description;

<<<<<<< HEAD
	public Item(int inventoryItemId, int ownerId){
		this.inventoryItemId = inventoryItemId;
		this.ownerId = ownerId;
	}

	public Item(){};
=======
	private Integer price;
	private Integer containingStoreId;

	public Item(int inventoryItemId, int ownerId, Integer price, Integer containingStoreId){
		this.inventoryItemId = inventoryItemId;
		this.ownerId = ownerId;
		this.price = price;
		this.containingStoreId = containingStoreId;

	}

	public Item(){}
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

	public int getOwnerId() {
		return ownerId;
	}

<<<<<<< HEAD
	public void setOwnerId(int ownerId) {
=======
	public void setOwnerId(Integer ownerId) {
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
		this.ownerId = ownerId;
	}

	public int getInventoryItemId() {
		return inventoryItemId;
	}

	public void setInventoryItemId(int inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}

	public ItemDescription getDescription() {
		return description;
	}

	public void setDescription(ItemDescription description) {
		this.description = description;
	}

<<<<<<< HEAD
=======
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getContainingStoreId() {
		return containingStoreId;
	}

	public void setContainingStoreId(Integer storeId) {
		this.containingStoreId = storeId;
	}
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

	@Entity
	@Table(name="itemconfigs")
	public static class ItemDescription {

		@Id
		int itemConfigID;
		String itemName;
		String itemDescription;

		public ItemDescription(){}

		public ItemDescription(int itemConfigID, String itemName, String itemDescription) {
			this.itemConfigID = itemConfigID;
			this.itemName = itemName;
			this.itemDescription = itemDescription;
		}

		public int getItemConfigID() {
			return itemConfigID;
		}

		public void setItemConfigID(int itemConfigID) {
			this.itemConfigID = itemConfigID;
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
	}
}
