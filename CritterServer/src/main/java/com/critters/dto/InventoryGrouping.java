package com.critters.dto;

import com.critters.DTO;
import com.critters.dal.entity.Item;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Jeremy on 1/17/2017.
 */
@XmlRootElement
public class InventoryGrouping extends DTO {
	public List<Item> inventoryItemsGrouped;

	public InventoryGrouping(List<Item> itemsGrouped) {
		this.inventoryItemsGrouped = itemsGrouped;
	}
	public InventoryGrouping(){}

	public List<Item> getInventoryItemsGrouped() {
		return inventoryItemsGrouped;
	}

	public void setInventoryItemsGrouped(List<Item> itemsGrouped) {
		this.inventoryItemsGrouped = itemsGrouped;
	}



}
