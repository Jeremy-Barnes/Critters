package com.critters.bll;

import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy on 7/1/2018.
 */
public class ItemsBLL {
	static final Logger logger = LoggerFactory.getLogger("application");

	public static List<Item> createNewItems(Map<Integer, Integer> itemConfigAndQuantity, Integer ownerID) {
		List<Integer> items = new ArrayList<Integer>();
		for(Map.Entry<Integer, Integer> cfgAndQty : itemConfigAndQuantity.entrySet()) {
			int qty = cfgAndQty.getValue();
			int itemCfgID = cfgAndQty.getKey();
			for (int i = 0; i < qty; i++) {
				items.add(itemCfgID);
			}
		}
		return createNewItems(items, ownerID);
	}

	public static List<Item> createNewItems(List<Integer> itemConfigIds, Integer ownerID) {
		List<Item> items = new ArrayList<Item>();
		try(DAL dal = new DAL()) {
			for(Integer cfgid : itemConfigIds) {
				Item.ItemDescription descID = new Item.ItemDescription(cfgid, "", "", "");
				Item item = new Item();
				item.setOwnerId(ownerID);
				item.setDescription(descID);
				items.add(item);

			}
			dal.beginTransaction();
			items = dal.items.save(items);
			dal.commitTransaction();
		} catch(Exception e) {
			logger.error("Couldn't create items - " + itemConfigIds + " ownerID: " + ownerID, e);
		}
		return items;
	}
}
