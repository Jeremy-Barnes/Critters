package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.InventoryGrouping;
import com.critters.dal.dto.entity.Item;
import com.critters.dal.dto.entity.NPCStoreRestockConfig;
import com.critters.dal.dto.entity.Store;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
import javax.resource.spi.InvalidPropertyException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/**
 * Created by Jeremy on 8/28/2016.
 */
public class CommerceBLL {

	public static void changeItemsPrice(Item[] items, User user) {
		List<Item> streamableItems = Arrays.asList(items);
		UserBLL.verifyUserInventoryIsLoaded(user);
		Stream<Item> resultant = user.getInventory().parallelStream().filter(i -> streamableItems.stream().anyMatch(si -> si.getInventoryItemId() == i.getInventoryItemId()));//.toArray(Item[]::new);
		if(resultant != null && resultant.count() == items.length) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				entityManager.getTransaction().begin();
				resultant.forEach(r ->
								  {
									  r.setPrice(streamableItems.stream().filter(is -> is.getInventoryItemId() == r.getInventoryItemId()).findFirst().get().getPrice());
									  entityManager.merge(r);
								  });
				entityManager.merge(user);
				entityManager.getTransaction().commit();
			} finally {
				entityManager.close();
			}
		}
	}

	public static void changeItemsStore(Item[] items, User user) {
		UserBLL.verifyUserInventoryIsLoaded(user);
		List<Item> streamableItems = Arrays.asList(items);

		Stream<Item> resultant = user.getInventory().parallelStream().filter(i -> streamableItems.stream().anyMatch(si -> si.getInventoryItemId() == i.getInventoryItemId()));//.toArray(Item[]::new);
		if(resultant != null && resultant.count() == items.length) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				entityManager.getTransaction().begin();
				resultant.forEach(r -> {
					r.setPrice(streamableItems.stream().filter(is -> is.getInventoryItemId() == r.getInventoryItemId()).findFirst().get().getPrice());
					entityManager.merge(r);
				});
				entityManager.merge(user);
				entityManager.getTransaction().commit();
			} finally {
				entityManager.close();
			}
		}
	}

	public static void changeItemsOwnerViaPurchase(Item[] items, User user) throws InvalidPropertyException {
		Item[] dbItems = getItems(items);
		List<Item> streamableItems = Arrays.asList(dbItems);

		user = UserBLL.getFullUser(user.getUserID());
		User owner  = UserBLL.getFullUser(dbItems[0].getOwnerId());
		UserBLL.verifyUserInventoryIsLoaded(user);

		if(dbItems != null && streamableItems.stream().allMatch(is -> is.getPrice() != null)) {
			int totalPrice = streamableItems.stream().mapToInt(Item::getPrice).sum();
			if(user.getCritterbuxx() >= totalPrice) {
				EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
				try {
					entityManager.getTransaction().begin();
					for(int i = 0; i < dbItems.length; i++) {
						dbItems[i].setOwnerId(user.getUserID());
						user.setCritterbuxx(user.getCritterbuxx() - dbItems[i].getPrice());
						if (owner != null) owner.setCritterbuxx(owner.getCritterbuxx() + dbItems[i].getPrice());
						dbItems[i].setPrice(null);
						dbItems[i].setContainingStoreId(null);
						entityManager.merge(dbItems[i]);
					}

					entityManager.merge(user);
					if(owner != null) entityManager.merge(owner);
					entityManager.getTransaction().commit();
				} finally {
					entityManager.close();
				}
			} else {
				throw new InvalidPropertyException("You don't have enough money for " +(items.length > 1 ? "these items." : "this item."));
			}
		}
	}

	public static Item[] getItems(Item[] items){
		List<Item> itemsList = Arrays.asList(items);
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		try {
			List<Item> dbItems = entityManager.createQuery("from Item where inventoryItemId in :ids")
											  .setParameter("ids", itemsList.stream().map(Item::getInventoryItemId).collect(Collectors.toList()))
											  .getResultList();
			return dbItems.toArray(new Item[0]);
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

	protected static List<InventoryGrouping> groupItems(List<Item> items){
		List<InventoryGrouping> inventory = new ArrayList<InventoryGrouping>();

		Map<Integer, List<Item>> itemMap = items.parallelStream().collect(Collectors.groupingBy(itm -> itm.getDescription().getItemConfigID()));
		Collection<List<Item>> itemsGrouped = itemMap.values();
		itemsGrouped.parallelStream().forEach(listitems -> {
			for (int j = 1; j < listitems.size(); j++) {
				listitems.get(j).setDescription(null);
			}
			inventory.add(new InventoryGrouping(listitems));
		});

		return inventory;
	}

	public static List<NPCStoreRestockConfig> getAllRestockConfigs(){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<NPCStoreRestockConfig> cfgs = entityManager.createQuery("from NPCStoreRestockConfig").getResultList();
			return cfgs;
		} catch (Exception ex){
			return null;
		} finally {
			entityManager.close();
		}
	}

	public static void restock(NPCStoreRestockConfig restock){
		List<Item> stock = restock.getStore().getStoreStock();

		int totalInStock = -1;
		if(stock == null || stock.size() == 0) {
			totalInStock = 0;
		} else if(restock.getSpecificItem() != null) {
			totalInStock = (int) stock.stream().filter(s -> s.getDescription().getItemConfigID() == restock.getSpecificItem()).count();
		} else if (restock.getSpecificClass() != null){
			totalInStock = (int) stock.stream().filter(s -> s.getDescription().getItemClass().getItemClassificationID() == restock.getSpecificClass()).count();
		}
		else if(restock.getRarityCeiling() != null && restock.getRarityFloor() != null) {
			totalInStock = (int) stock.stream().filter(s -> restock.getRarityFloor() >= s.getDescription().getRarity().getItemRarityTypeID()
					&& s.getDescription().getRarity().getItemRarityTypeID() >= restock.getRarityCeiling()).count();
		}
		int totalToGet = Math.min((restock.getMaxTotalQuantity() - totalInStock), restock.getMaxQuantityToAdd());
		if(totalToGet <= 0) {
			return;
		}

		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("restockRandomly", Item.ItemDescription.class);

			query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);

			query.setParameter(1, restock.getRarityCeiling() == null ? -1 : restock.getRarityCeiling());
			query.setParameter(2, restock.getRarityFloor() == null ? -1 : restock.getRarityFloor());
			query.setParameter(3, totalToGet);
			query.setParameter(4, restock.getSpecificClass() == null ? -1 : restock.getSpecificClass());
			query.setParameter(5, restock.getSpecificItem() == null ? -1 : restock.getSpecificItem());

			query.execute();
			List<Item.ItemDescription> results = query.getResultList();
			results.forEach(d -> {
				Item i = new Item();
				i.setDescription(d);
				i.setContainingStoreId(restock.getStore().getStoreConfigID());
				i.setPrice(50); //todo economics :(
				entityManager.persist(i);
			});
			entityManager.getTransaction().commit();
		} catch(Exception e) {
			throw e;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}
}
