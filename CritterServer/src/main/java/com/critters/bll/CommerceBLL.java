package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.Item;
import com.critters.dal.dto.entity.Store;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.resource.spi.InvalidPropertyException;
import java.util.List;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class CommerceBLL {

	public static void changeItemPrice(Item item, User user) {
		UserBLL.verifyUserInventoryIsLoaded(user);
		Item[] resultant = user.getInventory().parallelStream().filter(i -> i.getInventoryItemId() == item.getInventoryItemId()).toArray(Item[]::new);
		if(resultant != null && resultant.length != 0) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				entityManager.getTransaction().begin();
				resultant[0].setPrice(item.getPrice());

				entityManager.merge(user);
				entityManager.merge(resultant[0]);
				entityManager.getTransaction().commit();
			} finally {
				entityManager.close();
			}
		}
	}

	public static void changeItemStore(Item item, User user) {
		UserBLL.verifyUserInventoryIsLoaded(user);
		Item[] resultant = user.getInventory().parallelStream().filter(i -> i.getInventoryItemId() == item.getInventoryItemId()).toArray(Item[]::new);
		if(resultant != null && resultant.length != 0) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				entityManager.getTransaction().begin();
				resultant[0].setContainingStoreId(item.getContainingStoreId());
				entityManager.merge(user);
				entityManager.merge(resultant[0]);
				entityManager.getTransaction().commit();
			} finally {
				entityManager.close();
			}
		}
	}

	public static void changeItemOwnerViaPurchase(Item item, User user) throws InvalidPropertyException {
		Item dbItem = getItem(item);
		user = UserBLL.getFullUser(user.getUserID());
		User owner  = UserBLL.getFullUser(dbItem.getOwnerId());
		UserBLL.verifyUserInventoryIsLoaded(user);

		if(dbItem != null && dbItem.getPrice() != null) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				if(user.getCritterbuxx() >= dbItem.getPrice()) {
					entityManager.getTransaction().begin();
					dbItem.setOwnerId(user.getUserID());
					user.setCritterbuxx(user.getCritterbuxx() - dbItem.getPrice());
					owner.setCritterbuxx(owner.getCritterbuxx() + dbItem.getPrice());
					dbItem.setPrice(null);
					dbItem.setContainingStoreId(null);
					entityManager.merge(dbItem);
					entityManager.merge(user);
					entityManager.merge(owner);
					entityManager.getTransaction().commit();
				} else {
					throw new InvalidPropertyException("You don't have enough money for this item.");
				}
			} finally {
				entityManager.close();
			}
		}
	}

	public static Item getItem(Item item){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		try {
			Item dbItem = (Item) entityManager.createQuery("from Item where inventoryItemId = :id").setParameter("id", item.getInventoryItemId()).getSingleResult();
			return dbItem;
		} catch (PersistenceException ex) {
			return null; //no item found
		} finally {
			entityManager.close();
		}
	}

	public static Store createStore(Store store, User owner){
		store.setOwnerId(owner.getUserID());
		//todo contentfilter on store.description and store.name
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(store);
			entityManager.getTransaction().commit();
			return store;
		} catch(Exception e) {
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}

	}

	public static Store editStore(Store store, User owner){
		//todo contentfilter on store.description and store.name
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.merge(store);
			entityManager.getTransaction().commit();
			return store;
		} catch(Exception e) {
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}

	}

	public static Store getStore(Store store){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			Store dbStore = (Store) entityManager.createQuery("from Store where storeConfigID = :id").setParameter("id", store.getStoreConfigID()).getSingleResult();
			List<Item> stock = entityManager
					.createQuery("from Item where containingStoreId = :id and price != null")
					.setParameter("id", store.getStoreConfigID())
					.getResultList();
			dbStore.setStoreStock(stock);
			return dbStore;
		} catch (PersistenceException ex) {
			return null; //no items found
		} finally {
			entityManager.close();
		}
	}
}
