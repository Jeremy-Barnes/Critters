package com.critters.dal.dto.entity;

import com.critters.dal.dto.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Entity
@Table(name="inventoryitems")
public class Item extends DTO {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int inventoryItemId;
	private Integer ownerId;

	@ManyToOne
	@JoinColumn(name="itemTypeId", updatable = false)
	private ItemDescription description;

	private Integer price;
	private Integer containingStoreId;

	public Item(int inventoryItemId, int ownerId, Integer price, Integer containingStoreId){
		this.inventoryItemId = inventoryItemId;
		this.ownerId = ownerId;
		this.price = price;
		this.containingStoreId = containingStoreId;

	}

	public Item(){}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
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

	@Entity
	@Table(name="itemconfigs")
	public static class ItemDescription extends DTO {

		@Id
		int itemConfigID;
		String itemName;
		String itemDescription;
		String imagePath;

		@ManyToOne
		@JoinColumn(name="itemClass", updatable = false)
		private ItemClassification itemClass;

		@ManyToOne
		@JoinColumn(name="rarity", updatable = false)
		private ItemRarityType rarity;

		public ItemDescription(){}

		public ItemDescription(int itemConfigID, String itemName, String itemDescription, String imagePath) {
			this.itemConfigID = itemConfigID;
			this.itemName = itemName;
			this.itemDescription = itemDescription;
			this.imagePath = imagePath;
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

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

		public ItemClassification getItemClass() {
			return itemClass;
		}

		public void setItemClass(ItemClassification itemClass) {
			this.itemClass = itemClass;
		}

		public ItemRarityType getRarity() {
			return rarity;
		}

		public void setRarity(ItemRarityType rarity) {
			this.rarity = rarity;
		}


	}

	@Entity
	@Table(name="itemClassifications")
	public static class ItemClassification extends DTO {
		@Id
		int itemClassificationID;
		String name;

		public ItemClassification(int itemClassificationID, String name) {
			this.itemClassificationID = itemClassificationID;
			this.name = name;
		}

		public ItemClassification(){}

		public int getItemClassificationID() {
			return itemClassificationID;
		}

		public void setItemClassificationID(int itemClassificationID) {
			this.itemClassificationID = itemClassificationID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Entity
	@Table(name="itemRarityTypes")
	public static class ItemRarityType extends DTO {
		@Id
		int itemRarityTypeID;
		String name;

		public ItemRarityType(int itemRarityTypeID, String name) {
			this.itemRarityTypeID = itemRarityTypeID;
			this.name = name;
		}

		public ItemRarityType(){}

		public int getItemRarityTypeID() {
			return itemRarityTypeID;
		}

		public void setItemRarityTypeID(int itemRarityTypeID) {
			this.itemRarityTypeID = itemRarityTypeID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
