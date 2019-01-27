package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.critters.dal.accessors.DAL;
import com.critters.dto.InventoryGrouping;
import com.critters.dal.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.resource.spi.InvalidPropertyException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class CommerceBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static boolean changeItemsPrice(Item[] items, int userID) {
		List<Item> streamableItems = Arrays.asList(items);

		try(DAL dal = new DAL()){
			User user = dal.users.getUserByID(userID);
			if(user == null) return false;
			user.initializeInventory();
			Stream<Item> ownedByUser = user.getInventory().parallelStream().filter(i -> streamableItems.stream().anyMatch(si -> si.getInventoryItemId() == i.getInventoryItemId()));
			if(ownedByUser != null && ownedByUser.count() == items.length) {
				ownedByUser.forEach(r -> r.setPrice(streamableItems.stream().filter(is -> is.getInventoryItemId() == r.getInventoryItemId()).findFirst().get().getPrice()));
				dal.beginTransaction();
				dal.items.save(ownedByUser.collect(Collectors.toList()));
				if(!dal.commitTransaction()) {
					String itemArray = "";
					List<Item> listItems = Arrays.asList(items);
					for(Item item : listItems){
						itemArray += "\n" + item.toString();
					}
					logger.error("Could not alter price of one of these items " + itemArray);
					return false;
				}
			}
		}
		return true;
	}

	public static boolean changeItemsStore(Item[] items, User user) {
		try(DAL dal = new DAL()) {
			User dbUser = dal.users.getUserByID(user.getUserID());
			dbUser.initializeInventory();
			List<Item> streamableItems = Arrays.asList(items);
			Stream<Item> itemsOwnedByUser = user.getInventory().parallelStream().filter(i -> streamableItems.stream().anyMatch(si -> si.getInventoryItemId() == i.getInventoryItemId()));
			if(itemsOwnedByUser != null && itemsOwnedByUser.count() == items.length) {
				itemsOwnedByUser.forEach(i ->
					i.setPrice(streamableItems.stream().filter(is -> is.getInventoryItemId() == i.getInventoryItemId()).findFirst().get().getPrice()));

				dal.beginTransaction();
				dal.items.save(itemsOwnedByUser.collect(Collectors.toList()));
				dal.users.save(dbUser);
				if(!dal.commitTransaction()) {
					String itemArray = "";
					List<Item> listItems = Arrays.asList(items);
					for(Item item : listItems){
						itemArray += "\n" + item.toString();
					}
					logger.error("Could not put one of these items in the store " + itemArray + " user:" + user.toString());
					return false;
				}
			}
		}
		return true;
	}

	public static void changeItemsOwnerViaPurchase(Item[] purchaseItems, User buyer) {
		try(DAL dal = new DAL()) {
			List<Item> items = dal.items.getItems(purchaseItems);
			if(items == null) return;
			buyer = dal.users.getUserByID(buyer.getUserID());
			User owner = items.get(0).getOwnerId() == null ? null : dal.users.getUserByID(items.get(0).getOwnerId());
			buyer.initializeInventory();

			if(buyer != null && Extensions.isNullOrEmpty(items) && items.stream().allMatch(is -> is.getPrice() != null)) {
				int totalPrice = items.stream().mapToInt(Item::getPrice).sum();
				if(buyer.getCritterbuxx() >= totalPrice) {
					for(int i = 0; i < items.size(); i++) {
						Item item = items.get(i);
						item.setOwnerId(buyer.getUserID());
						item.setPrice(null);
						item.setContainingStoreId(null);
					}
					buyer.setCritterbuxx(buyer.getCritterbuxx() - totalPrice);
					if (owner != null) owner.setCritterbuxx(owner.getCritterbuxx() + totalPrice);
					dal.beginTransaction();
					dal.items.save(items);
					dal.users.save(buyer);
					if(owner != null) dal.users.save(owner);
					dal.commitTransaction();
				} else {
					throw new InvalidPropertyException("You don't have enough money for " +(items.size() > 1 ? "these items." : "this item."));
				}
			}
		}  catch(Exception e) {
			String itemArray = "";
			List<Item> listItems = Arrays.asList(purchaseItems);
			for(Item item : listItems){
				itemArray += "\n" + item.toString();
			}
			logger.error("Could transfer one of these items to the new owner " + buyer.toString()  + "\n" + itemArray, e);
		}
	}

	public static Item[] getItems(Item[] items) {
		try(DAL dal = new DAL()){
			return dal.items.getItems(items).toArray(new Item[0]);
		}
	}

	public static Store createStore(Store store, StoreBackgroundImageOption background, StoreClerkImageOption clerk, User owner) {
		store.setOwnerId(owner.getUserID());
		//todo contentfilter on store.description and store.name

		try(DAL dal = new DAL()) {
			//WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never from User Input. That way porn lies.
			setStoreImagesSafely(background, clerk, store, dal);

			dal.beginTransaction();
			dal.shops.save(store);
			if (!dal.commitTransaction()) {
				logger.error("Something went wrong creating this store " + store.toString()
									 + " with this background " + background.toString()
									 + " with this clerk " + clerk.toString()
									 + " for this user " + owner.toString());
				store = null;
			}
		}
		return store;
	}

	private static void setStoreImagesSafely(StoreBackgroundImageOption background, StoreClerkImageOption clerk, Store store, DAL dal){
		if (background != null) { //keep out the porn
			background = dal.configuration.getStoreBackgroundImageOption(background.getStoreBackgroundImageOptionID());
		}
		store.setStoreBackgroundImagePath(background != null ? background.getImagePath() : null);

		if (clerk != null) {
			clerk = dal.configuration.getStoreClerkImageOption(clerk.getStoreClerkImageOptionID());
		}
		store.setStoreClerkImagePath(clerk != null ? clerk.getImagePath() : null);
	}

	public static Store editStore(Store store, StoreBackgroundImageOption background, StoreClerkImageOption clerk, User owner) {
		//todo contentfilter on store.description and store.name

		try(DAL dal = new DAL()){
			//WARNING NEVER REMOVE THIS FUNCTIONALITY. Images must come from our DB, never from User Input. That way porn lies.
			setStoreImagesSafely(background, clerk, store, dal);
			dal.beginTransaction();
			dal.shops.save(store);
			if(!dal.commitTransaction()){
				logger.error("Something went wrong updating this store " + store.toString()
									 + " with this background " + background.toString()
									 + " with this clerk " + clerk.toString()
									 + " for this user " + owner.toString());
				store = null;
			}
			return store;
		}
	}

	public static Store getStore(int storeID) {
		try(DAL dal = new DAL()){
			Store dbStore = dal.shops.getShopByID(storeID);
			if(dbStore == null) return null;
			List<Item> stock = dal.items.getItemsByStoreID(storeID);
			dbStore.setStoreStock(groupItems(stock));
			dbStore.setStoreStockDecomposed(stock);
			return dbStore;
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
		try(DAL dal = new DAL()) {
			return dal.configuration.getAllRestockConfigs();
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

		try(DAL dal = new DAL()) {
			List<Item.ItemDescription> results = dal.configuration.executeStoreRandomRestockSproc(restock, totalToGet);
			if(results == null) return;
			List<Item> newItems = new ArrayList<Item>(totalToGet);
			results.forEach(d -> {
				Item i = new Item();
				i.setDescription(d);
				i.setContainingStoreId(restock.getStore().getStoreConfigID());
				i.setPrice(50); //todo economics :(
			});
			dal.beginTransaction();
			dal.items.save(newItems);
			if(!dal.commitTransaction()) {
				String itemArray = "";
				for(Item.ItemDescription item : results){
					itemArray += "\n" + item.toString();
				}
				logger.error("Something went wrong while trying to put these items on the shelves " + itemArray);
			}
		}
	}

	public static StoreBackgroundImageOption getStoreBackgroundImageOptionForPresentation(int id)  {
		try(DAL dal = new DAL()) {
			return dal.configuration.getStoreBackgroundImageOption(id);
		}
	}

	public static StoreBackgroundImageOption[] getStoreBackgroundImageOptionsForPresentation() {
		try(DAL dal = new DAL()) {
			return dal.configuration.getStoreBackgroundImageOptions().toArray(new StoreBackgroundImageOption[0]);
		}
	}

	public static StoreClerkImageOption getStoreClerkImageOptionForPresentation(int id) {
		try(DAL dal = new DAL()) {
			return dal.configuration.getStoreClerkImageOption(id);
		}
	}

	public static StoreClerkImageOption[] getStoreClerkImageOptionsForPresentation() { //todo caching
		try(DAL dal = new DAL()) {
			return dal.configuration.getStoreClerkImageOptions().toArray(new StoreClerkImageOption[0]);
		}
	}
}
