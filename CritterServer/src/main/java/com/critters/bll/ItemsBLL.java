package com.critters.bll;

import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.Item;
import com.critters.dal.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

	public static List<Item> getAbandonedItems(boolean attachToEvent) {
		try(DAL dal = new DAL()) {
			List<Item> items = dal.items.getItemsWithoutOwner(20, true);
			if(items.size() < 20) items.addAll(dal.items.getItemsWithoutOwner(10, false));
			LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
			List<Item> filteredItems = new ArrayList<Item>();
			for(Item i : items) {
				if(i.getAttachedToEvent() == null || now.minusHours(24).isAfter(i.getAttachedToEvent())) //only let items lock for one day
				i.setAttachedToEvent(now);
				filteredItems.add(i);
			}
			return dal.items.save(filteredItems);
		} catch(Exception e) {
			logger.error("Couldn't find floor items", e);
		}
		return null;
	}

	public static boolean giveDiscardedInventoryItems(List<Item> items, User recipient) {
		try (DAL dal = new DAL()) {
			List<Item> dbItems = dal.items.getItems(items);
			if (!dbItems.stream().allMatch(i -> i.getOwnerId() == null)) {
				return false;
			}
			recipient.initializeInventory();

			for (Item i : dbItems) {
				recipient.getInventory().add(i);
				i.setOwnerId(recipient.getUserID());
				i.setAttachedToEvent(null);
			}
			dal.beginTransaction();
			dal.items.save(dbItems);
			dal.users.save(recipient);
			dal.commitTransaction();
			items = dbItems;
			return true;
		} catch(Exception ex){
			logger.error("Couldn't give floor items to user", ex);
		}
		return false;
	}
}
