package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by Jeremy on 8/14/2016.
 */
@Entity
@Table(name="npcStoreRestockConfigs")
public class NPCStoreRestockConfig {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int npcStoreStockConfigID;
	private float percentOdds;
	private int maxQuantityToAdd;
	private int maxQuantityTotalQuantity;
	private int specificClass;
	private int rarityFloor;
	private int rarityCeiling;
	private int specificItem;
	private int storeID;

	public NPCStoreRestockConfig(float percentOdds, int maxQuantityToAdd, int maxQuantityTotalQuantity, int specificClass, int rarityFloor, int rarityCeiling, int specificItem, int storeID) {
		this.percentOdds = percentOdds;
		this.maxQuantityToAdd = maxQuantityToAdd;
		this.maxQuantityTotalQuantity = maxQuantityTotalQuantity;
		this.specificClass = specificClass;
		this.rarityFloor = rarityFloor;
		this.rarityCeiling = rarityCeiling;
		this.specificItem = specificItem;
		this.storeID = storeID;
	}

	public NPCStoreRestockConfig(){}

	public int getNpcStoreStockConfigID() {
		return npcStoreStockConfigID;
	}

	public void setNpcStoreStockConfigID(int npcStoreStockConfigID) {
		this.npcStoreStockConfigID = npcStoreStockConfigID;
	}

	public float getPercentOdds() {
		return percentOdds;
	}

	public void setPercentOdds(float percentOdds) {
		this.percentOdds = percentOdds;
	}

	public int getMaxQuantityToAdd() {
		return maxQuantityToAdd;
	}

	public void setMaxQuantityToAdd(int maxQuantityToAdd) {
		this.maxQuantityToAdd = maxQuantityToAdd;
	}

	public int getMaxQuantityTotalQuantity() {
		return maxQuantityTotalQuantity;
	}

	public void setMaxQuantityTotalQuantity(int maxQuantityTotalQuantity) {
		this.maxQuantityTotalQuantity = maxQuantityTotalQuantity;
	}

	public int getSpecificClass() {
		return specificClass;
	}

	public void setSpecificClass(int specificClass) {
		this.specificClass = specificClass;
	}

	public int getRarityFloor() {
		return rarityFloor;
	}

	public void setRarityFloor(int rarityFloor) {
		this.rarityFloor = rarityFloor;
	}

	public int getRarityCeiling() {
		return rarityCeiling;
	}

	public void setRarityCeiling(int rarityCeiling) {
		this.rarityCeiling = rarityCeiling;
	}

	public int getSpecificItem() {
		return specificItem;
	}

	public void setSpecificItem(int specificItem) {
		this.specificItem = specificItem;
	}

	public int getStoreID() {
		return storeID;
	}

	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}
}
