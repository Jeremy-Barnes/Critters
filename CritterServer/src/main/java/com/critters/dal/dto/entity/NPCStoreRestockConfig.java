package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * Created by Jeremy on 8/14/2016.
 */
@Entity
@Table(name="npcStoreRestockConfigs")
public class NPCStoreRestockConfig {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Integer npcStoreStockConfigID;
	private float percentOdds;
	private Integer maxQuantityToAdd;
	private Integer maxTotalQuantity;
	private Integer specificClass;
	private Integer rarityFloor;
	private Integer rarityCeiling;
	private Integer specificItem;

	@ManyToOne
	@JoinColumn(name="storeid", updatable = false)
	private Store store;

	public NPCStoreRestockConfig(float percentOdds, Integer maxQuantityToAdd, Integer maxTotalQuantity, Integer specificClass, Integer rarityFloor, Integer rarityCeiling, Integer specificItem, Store store) {
		this.percentOdds = percentOdds;
		this.maxQuantityToAdd = maxQuantityToAdd;
		this.maxTotalQuantity = maxTotalQuantity;
		this.specificClass = specificClass;
		this.rarityFloor = rarityFloor;
		this.rarityCeiling = rarityCeiling;
		this.specificItem = specificItem;
		this.store = store;
	}

	public NPCStoreRestockConfig(){}

	public Integer getNpcStoreStockConfigID() {
		return npcStoreStockConfigID;
	}

	public void setNpcStoreStockConfigID(Integer npcStoreStockConfigID) {
		this.npcStoreStockConfigID = npcStoreStockConfigID;
	}

	public float getPercentOdds() {
		return percentOdds;
	}

	public void setPercentOdds(float percentOdds) {
		this.percentOdds = percentOdds;
	}

	public Integer getMaxQuantityToAdd() {
		return maxQuantityToAdd;
	}

	public void setMaxQuantityToAdd(Integer maxQuantityToAdd) {
		this.maxQuantityToAdd = maxQuantityToAdd;
	}

	public Integer getMaxTotalQuantity() {
		return maxTotalQuantity;
	}

	public void setMaxTotalQuantity(Integer maxTotalQuantity) {
		this.maxTotalQuantity = maxTotalQuantity;
	}

	public Integer getSpecificClass() {
		return specificClass;
	}

	public void setSpecificClass(Integer specificClass) {
		this.specificClass = specificClass;
	}

	public Integer getRarityFloor() {
		return rarityFloor;
	}

	public void setRarityFloor(Integer rarityFloor) {
		this.rarityFloor = rarityFloor;
	}

	public Integer getRarityCeiling() {
		return rarityCeiling;
	}

	public void setRarityCeiling(Integer rarityCeiling) {
		this.rarityCeiling = rarityCeiling;
	}

	public Integer getSpecificItem() {
		return specificItem;
	}

	public void setSpecificItem(Integer specificItem) {
		this.specificItem = specificItem;
	}

	public Store getStore() {
		return store;
	}

	public void setStoreID(Store store) {
		this.store = store;
	}
}
