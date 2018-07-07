package com.critters.bll;

import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy on 7/1/2018.
 */
public class ItemsBLL {

	public static List<Item> createNewItems(Map<Integer, Integer> itemConfigAndQuantity, Integer ownerID) {
		List<Item> items = new ArrayList<Item>();
		try(DAL dal = new DAL()) {
				for(Map.Entry<Integer, Integer> cfgAndQty : itemConfigAndQuantity.entrySet()) {
					int qty = cfgAndQty.getValue();
					int itemCfgID = cfgAndQty.getKey();
					Item.ItemDescription descID = new Item.ItemDescription(itemCfgID, "", "", "");
					for (int i = 0; i < qty; i++) {
						Item item = new Item();
						item.setOwnerId(ownerID);
						item.setDescription(descID);
						items.add(item);
					}
				}
				dal.beginTransaction();
				items = dal.items.save(items);
				dal.commitTransaction();
			} catch(Exception e) {
				System.out.print(e.getMessage());
			}
		return items;
	}
}
