package com.critters.dal.accessors;

import com.critters.dal.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class ConfigAccessor{
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	ConfigAccessor(EntityManager hibernateHelper){
		sql = hibernateHelper;
	}

	//todo caching
	public List<UserImageOption> getUserImages() {
		List<UserImageOption> dbItems = null;
		try {
			dbItems = sql.createQuery("from UserImageOption").getResultList();
		}catch (PersistenceException ex) {
			logger.error("Database error searching for images!", ex);
		}
		return dbItems;
	}

	public UserImageOption getUserImage(int id) {
		UserImageOption dbItem = null;
		try {
			dbItem = (UserImageOption) sql.createQuery("from UserImageOption where userImageOptionID = :id").setParameter("id", id)
										  .getSingleResult();
		} catch (NoResultException nrex) {
			logger.info("No such image option found with id " + id, nrex);
		} catch (PersistenceException ex) {
			logger.error("Database error searching for id " + id, ex);
		}
		return dbItem;
	}

	public StoreBackgroundImageOption getStoreBackgroundImageOption(int id)  { //todo caching
		StoreBackgroundImageOption image = null;
		try {
			image = (StoreBackgroundImageOption) sql.createQuery("from StoreBackgroundImageOption where storeBackgroundImageOptionID = :id")
																						 .setParameter("id", id)
																						 .getSingleResult();
		}  catch (NoResultException nrex) {
			logger.info("Failed to find store background image " + id, nrex);
		} catch (PersistenceException ex) {
			logger.error("Something is bananas wrong with the database if it can't find images!", ex);
		}
		return image;
	}

	public List<StoreBackgroundImageOption> getStoreBackgroundImageOptions() { //todo caching
		List<StoreBackgroundImageOption> images = null;
		try {
			images = sql.createQuery("from StoreBackgroundImageOption").getResultList();
		} catch (PersistenceException ex) {
			logger.error("Something is bananas wrong with the database if it can't find images!", ex);
		}
		return images;
	}

	public StoreClerkImageOption getStoreClerkImageOption(int id) { //todo caching
		StoreClerkImageOption image = null;
		try {
			image = (StoreClerkImageOption) sql.createQuery("from StoreClerkImageOption where storeClerkImageOptionID = :id")
																			   .setParameter("id", id)
																			   .getSingleResult();
		} catch (NoResultException nrex) {
			logger.info("Failed to find store clerk image " + id, nrex);
		} catch (PersistenceException ex) {
			logger.error("Something is bananas wrong with the database if it can't find images!", ex);
		}
		return image;
	}

	public List<StoreClerkImageOption> getStoreClerkImageOptions() {
		List<StoreClerkImageOption> images = null;
		try {
			images = sql.createQuery("from StoreClerkImageOption").getResultList();
		} catch (PersistenceException ex) {
			logger.error("Something is bananas wrong with the database if it can't find images!", ex);
		}
		return images;
	}

	public List<NPCStoreRestockConfig> getAllRestockConfigs(){
		List<NPCStoreRestockConfig> cfgs = null;
		try {
			cfgs = sql.createQuery("from NPCStoreRestockConfig").getResultList();
		} catch (Exception ex){
			logger.debug("Couldn't get restock configs", ex);
			return new ArrayList<NPCStoreRestockConfig>();
		}
		return cfgs;
	}

	public List<Item.ItemDescription> executeStoreRandomRestockSproc(NPCStoreRestockConfig restock, int totalResultsToGet) {
		List<Item.ItemDescription> results = null;
		try {
			StoredProcedureQuery query = sql.createStoredProcedureQuery("restockRandomly", Item.ItemDescription.class);

			query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
			query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);

			query.setParameter(1, restock.getRarityCeiling() == null ? -1 : restock.getRarityCeiling());
			query.setParameter(2, restock.getRarityFloor() == null ? -1 : restock.getRarityFloor());
			query.setParameter(3, totalResultsToGet);
			query.setParameter(4, restock.getSpecificClass() == null ? -1 : restock.getSpecificClass());
			query.setParameter(5, restock.getSpecificItem() == null ? -1 : restock.getSpecificItem());

			query.execute();
			results = query.getResultList();
		} catch(Exception e){
			logger.error("An error happened restocking with restock config :" + restock.toString(), e);
		}
		return results;
	}

	public List<Pet.PetSpecies> getPetSpecies() {
		try {
			List<Pet.PetSpecies> options = sql.createQuery("from Pet$PetSpecies").getResultList();
			return options;
		} catch(Exception e) {
			logger.error("SOMEHOW THERE ARE NO SPECIES", e);
			return null;
		}
	}

	public List<Pet.PetColor> getPetColors() {
		try {
			List<Pet.PetColor> options = sql.createQuery("from Pet$PetColor").getResultList();
			return options;
		} catch(Exception e) {
			logger.error("SOMEHOW THERE ARE NO COLORS", e);
			return null;
		}
	}

	public <T> List<T> save(List<T> configs) {
		List<T> dbItems = new ArrayList<T>();
		configs.forEach(i -> dbItems.add(save(i)));
		return dbItems;
	}

	public <T> T save(T config) {
		return sql.merge(config);
	}





}
