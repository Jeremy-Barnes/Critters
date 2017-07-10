package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.InventoryGrouping;
import com.critters.dal.dto.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.resource.spi.InvalidPropertyException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class CommerceBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static void changeItemsPrice(Item[] items, User user) throws Exception {
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
			} catch(Exception e) {
				String itemArray = "";
				List<Item> listItems = Arrays.asList(items);
				for(Item item : listItems){
					itemArray += "\n" + item.toString();
				}
				logger.error("Could not alter price of one of these items " + itemArray, e);
				throw new Exception("Couldn't alter the price of your items! Contact an admin about this, please.");
			} finally {
				if(entityManager.getTransaction().isActive()){
					entityManager.getTransaction().rollback();
				}
				entityManager.close();
			}
		}
	}

	public static void changeItemsStore(Item[] items, User user) throws Exception {
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
			}  catch(Exception e) {
				String itemArray = "";
				List<Item> listItems = Arrays.asList(items);
				for(Item item : listItems){
					itemArray += "\n" + item.toString();
				}
				logger.error("Could not put one of these items in the store " + itemArray + " user:" + user.toString() , e);
				throw new Exception("Couldn't move your items to your store! Contact an admin about this, please.");
			} finally {
				if(entityManager.getTransaction().isActive()){
					entityManager.getTransaction().rollback();
				}
				entityManager.close();
			}
		}
	}

	public static void changeItemsOwnerViaPurchase(Item[] items, User user) throws Exception {
		Item[] dbItems = getItems(items);
		List<Item> streamableItems = Arrays.asList(dbItems);

		user = UserBLL.getFullUser(user.getUserID());
		User owner = null; //could be an NPC!
		if(dbItems[0].getOwnerId() != null)
			owner  = UserBLL.getFullUser(dbItems[0].getOwnerId());
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
				}  catch(Exception e) {
					String itemArray = "";
					List<Item> listItems = Arrays.asList(items);
					for(Item item : listItems){
						itemArray += "\n" + item.toString();
					}
					logger.error("Could transfer one of these items to the new owner " + user.toString()  + "\n" + itemArray, e);
					throw new Exception("Couldn't complete this sale! Contact an admin about this, please.");
				} finally {
					if(entityManager.getTransaction().isActive()){
						entityManager.getTransaction().rollback();
					}
					entityManager.close();
				}
			} else {
				throw new InvalidPropertyException("You don't have enough money for " +(items.length > 1 ? "these items." : "this item."));
			}
		}
	}

	public static Item[] getItems(Item[] items) throws Exception {
		List<Item> itemsList = Arrays.asList(items);
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();

		try {
			List<Item> dbItems = entityManager.createQuery("from Item where inventoryItemId in :ids")
											  .setParameter("ids", itemsList.stream().map(Item::getInventoryItemId).collect(Collectors.toList()))
											  .getResultList();
			return dbItems.toArray(new Item[0]);
		} catch (PersistenceException ex) {
			throw new Exception("Something critical went wrong in the database. Please contact an admin, your items should be safe.");
		} finally {
			entityManager.close();
		}
	}

	public static Store createStore(Store store, StoreBackgroundImageOption background, StoreClerkImageOption clerk, User owner) throws Exception {
		store.setOwnerId(owner.getUserID());

		if(background != null) { //WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never
			//from User Input. That way porn lies.
			background = getStoreBackgroundImageOption(background.getStoreBackgroundImageOptionID());
		}
		store.setStoreBackgroundImagePath(background != null ? background.getImagePath() : null);


		if(clerk != null) {//WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never
			//from User Input. That way porn lies.
			clerk = getStoreClerkImageOption(clerk.getStoreClerkImageOptionID());
		}
		store.setStoreClerkImagePath(clerk != null ? clerk.getImagePath() : null);


		//todo contentfilter on store.description and store.name
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(store);
			entityManager.getTransaction().commit();
			return store;
		} catch(Exception e) {
			logger.error("Something went wrong creating this store " + store.toString()
					+ " with this background " + background.toString()
					+ " with this clerk " + clerk.toString()
					+ " for this user " + owner.toString(), e);
			throw new Exception("Couldn't create your store. Please contact an admin.");
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static Store editStore(Store store, StoreBackgroundImageOption background, StoreClerkImageOption clerk, User owner) throws Exception {
		//todo contentfilter on store.description and store.name

		if(background != null) { //WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never
			//from User Input. That way porn lies.
			background = getStoreBackgroundImageOption(background.getStoreBackgroundImageOptionID());
		}
		store.setStoreBackgroundImagePath(background != null ? background.getImagePath() : null);

		if(clerk != null) {//WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never
			//from User Input. That way porn lies.
			clerk = getStoreClerkImageOption(clerk.getStoreClerkImageOptionID());
		}
		store.setStoreClerkImagePath(clerk != null ? clerk.getImagePath() : null);

		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.merge(store);
			entityManager.getTransaction().commit();
			return store;
		} catch(Exception e) {
			logger.error("Something went wrong updating this store " + store.toString()
								 + " with this background " + background.toString()
								 + " with this clerk " + clerk.toString()
								 + " for this user " + owner.toString(), e);
			throw new Exception("Couldn't create your store. Please contact an admin.");
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static Store getStore(int storeID) throws Exception {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			Store dbStore = (Store) entityManager.createQuery("from Store where storeConfigID = :id").setParameter("id", storeID).getSingleResult();
			List<Item> stock = entityManager
					.createQuery("from Item where containingStoreId = :id and price != null")
					.setParameter("id", storeID)
					.getResultList();
			dbStore.setStoreStock(groupItems(stock));
			dbStore.setStoreStockDecomposed(stock);
			return dbStore;
		}  catch (NoResultException nrex) {
			return null;
		} catch (PersistenceException ex) {
			throw new Exception("Something went very wrong getting this store. Contact an admin!");
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
			logger.debug("Couldn't get restock configs", ex);
			return new ArrayList<NPCStoreRestockConfig>();
		} finally {
			entityManager.close();
		}
	}

	public static void restock(NPCStoreRestockConfig restock){
		List<Item> stock = restock.getStore().getStoreStockDecomposed();
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
		}  catch(Exception e) {
			logger.error("Something went wrong during this restock " + restock.toString(), e);
			throw e;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static StoreBackgroundImageOption getStoreBackgroundImageOption(int id) throws Exception { //todo caching
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			StoreBackgroundImageOption image = (StoreBackgroundImageOption) entityManager.createQuery("from StoreBackgroundImageOption where storeBackgroundImageOptionID = :id")
																						 .setParameter("id", id)
																						 .getSingleResult();
			return image;
		}  catch (NoResultException nrex) {
			return null;
		} catch (PersistenceException ex) {
			throw new Exception("Something is bananas wrong with the database if it can't find images, tell an admin!");
		} finally {
			entityManager.close();
		}
	}

	public static StoreBackgroundImageOption[] getStoreBackgroundImageOptions() throws Exception { //todo caching
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			StoreBackgroundImageOption[] images = (StoreBackgroundImageOption[]) entityManager.createQuery("from StoreBackgroundImageOption")
																							  .getResultList().toArray(new StoreBackgroundImageOption[0]);
			return images;
		} catch (PersistenceException ex) {
			throw new Exception("Something is bananas wrong with the database if it can't find images, tell an admin!");
		} finally {
			entityManager.close();
		}
	}

	public static StoreClerkImageOption getStoreClerkImageOption(int id) throws Exception { //todo caching
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			StoreClerkImageOption image = (StoreClerkImageOption) entityManager.createQuery("from StoreClerkImageOption where storeClerkImageOptionID = :id")
																			   .setParameter("id", id)
																			   .getSingleResult();
			return image;
		} catch (NoResultException nrex) {
			return null;
		} catch (PersistenceException ex) {
			throw new Exception("Something is bananas wrong with the database if it can't find images, tell an admin!");
		} finally {
			entityManager.close();
		}
	}

	public static StoreClerkImageOption[] getStoreClerkImageOptions() throws Exception { //todo caching
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			StoreClerkImageOption[] images = (StoreClerkImageOption[]) entityManager.createQuery("from StoreClerkImageOption")
																					.getResultList().toArray(new StoreClerkImageOption[0]);
			return images;
		} catch (PersistenceException ex) {
			throw new Exception("Something is bananas wrong with the database if it can't find images, tell an admin!");
		} finally {
			entityManager.close();
		}
	}
}
