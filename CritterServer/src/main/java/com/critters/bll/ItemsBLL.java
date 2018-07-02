package com.critters.bll;

import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.Item;

import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy on 7/1/2018.
 */
public class ItemsBLL {

	public static List<Item> createNewItems(Map<Integer, Integer> itemConfigAndQuantity){
		for(Map.Entry<Integer, Integer> cfgAndQty : itemConfigAndQuantity.entrySet()) {
			int qty = cfgAndQty.getValue();
			int itemCfgID = cfgAndQty.getKey();
			try(DAL dal = new DAL()) {


				Item[] items = new Item[qty];
				for (int i = 0; i < qty; i++) {
					Item item = new Item();
					items[i] = item;
				}
			}
		}
		return null;
	}
}
