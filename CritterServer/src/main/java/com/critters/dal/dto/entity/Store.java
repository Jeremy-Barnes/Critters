package com.critters.dal.dto.entity;

import com.critters.dal.dto.DTO;
import com.critters.dal.dto.InventoryGrouping;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;


/**
 * Created by Jeremy on 12/4/2016.
 */
@Entity
@Table(name="storeConfigs")
public class Store extends DTO {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int storeConfigID;

	private Integer ownerId;
	private String description;
	private String name;
	private String storeClerkImagePath;
	private String storeBackgroundImagePath;

	@Transient
	private List<InventoryGrouping> storeStock;
	@Transient
	@XmlTransient
	private transient List<Item> storeStockDecomposed;

	public Store(int storeConfigID, Integer ownerId, String name, String description, String storeClerkImagePath, String storeBackgroundImagePath) {
		this.storeConfigID = storeConfigID;
		this.ownerId = ownerId;
		this.name = name;
		this.description = description;
		this.storeClerkImagePath = storeClerkImagePath;
		this.storeBackgroundImagePath = storeBackgroundImagePath;
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
	
	public String getStoreBackgroundImagePath() {
		return storeBackgroundImagePath;
	}

	public void setStoreBackgroundImagePath(String storeBackgroundImagePath) {
		this.storeBackgroundImagePath = storeBackgroundImagePath;	
	}
	
	public String getStoreClerkImagePath() {
		return storeClerkImagePath;
	}

	public void setStoreClerkImagePath(String storeClerkImagePath) {
		this.storeClerkImagePath = storeClerkImagePath;
	}

	public List<InventoryGrouping> getStoreStock() {
		return storeStock;
	}

	public void setStoreStock(List<InventoryGrouping> stock) {
		this.storeStock = stock;
	}
	@XmlTransient
	public List<Item> getStoreStockDecomposed() {
		return storeStockDecomposed;
	}
	@XmlTransient
	public void setStoreStockDecomposed(List<Item> stock) {
		this.storeStockDecomposed = stock;
	}

}
