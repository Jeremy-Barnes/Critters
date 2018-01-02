package com.critters.dal;

import com.critters.dal.dto.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class ItemAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	HibernateUtil.HibernateHelper sql;

	public ItemAccessor(HibernateUtil.HibernateHelper hibernateHelper){
		sql = hibernateHelper;
	}

	public List<Item> getItems(Item[] items) throws Exception {
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

	public void save(List<Item> items) {
		items.forEach(i -> save(i));
	}

	public void save(Item item) {
		sql.entityManager.merge(item);
	}




}
