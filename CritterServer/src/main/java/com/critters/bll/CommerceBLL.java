package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.Item;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.resource.spi.InvalidPropertyException;

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
				user.getInventory().remove(resultant[0]);
				resultant[0].setPrice(item.getPrice());

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
		user = UserBLL.getUser(user.getUserID());
		UserBLL.verifyUserInventoryIsLoaded(user);

		if(dbItem != null) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				if(user.getCritterbuxx() >= dbItem.getPrice()) {
					entityManager.getTransaction().begin();
					dbItem.setOwnerId(user.getUserID());
					user.setCritterbuxx(user.getCritterbuxx() - dbItem.getPrice());
					dbItem.setPrice(null);
					entityManager.merge(dbItem);
					entityManager.merge(user);
					entityManager.getTransaction().commit();
				} else {
					entityManager.getTransaction().rollback();
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


}
