package com.critters.dal.accessors;

import com.critters.dal.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class ItemAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	ItemAccessor(EntityManager hibernateHelper){
		sql = hibernateHelper;
	}

	public List<Item> getItems(Item[] items) {
		List<Item> itemsList = Arrays.asList(items);
		return getItems(itemsList.stream().map(Item::getInventoryItemId).collect(Collectors.toList()));
	}

	public List<Item> getItems(List<Integer> itemIDs) {
		List<Item> dbItems = null;
		try {
			dbItems = sql.createQuery("from Item where inventoryItemId in :ids")
											  .setParameter("ids", itemIDs)
											  .getResultList();
		} catch (PersistenceException ex) {
			logger.error("Something went wrong in the database while attempting to retrieve items: ", itemIDs);
		}
		return dbItems;
	}

	public List<Item> getItemsByOwnerID(int userID) {
		List<Item> dbItems = null;
		try {
			dbItems = sql.createQuery("from Item where ownerId = :id")
					.setParameter("id", userID)
					.getResultList();
		} catch (PersistenceException ex) {
			logger.error("Something went wrong in the database while attempting to retrieve items for user: " + userID, ex);
		}
		return dbItems;
	}

	public List<Item> getItemsByStoreID(int shopID) {
		List<Item> dbItems = null;
		try {
			dbItems = sql.createQuery("from Item where containingStoreId = :id and price != null")
						 .setParameter("id", shopID)
						 .getResultList();
		}  catch (NoResultException nrex) {
			logger.info("No such items found in store id " + shopID, nrex);
		}  catch (PersistenceException ex) {
			logger.error("Something went wrong in the database while attempting to retrieve items for shop: " + shopID, ex);
		}
		return dbItems;
	}


	public void save(List<Item> items) {
		items.forEach(i -> save(i));
	}

	public void save(Item item) {
		sql.merge(item);
	}




}
