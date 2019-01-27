package com.critters.dal.accessors;

import com.critters.dal.entity.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class StoreAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	StoreAccessor(EntityManager hibernateHelper){
		sql = hibernateHelper;
	}

	public Store getShopByID(int id){
		Store shops = null;
		try {
			shops = (Store) sql.createQuery("from Item where containingStoreId = :id and price != null")
							   .setParameter("id", id)
							   .getResultList();
		} catch (PersistenceException ex) {
			logger.debug("No active stores found with id " + id, ex);
		}
		return shops;
	}

	public void save(List<Store> shops) {
		shops.forEach(u -> save(u));
	}

	public void save(Store shop) {
		sql.merge(shop);
	}

}
