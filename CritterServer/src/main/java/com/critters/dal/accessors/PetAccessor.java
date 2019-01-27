package com.critters.dal.accessors;

import com.critters.dal.entity.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class PetAccessor {
	static final Logger logger = LoggerFactory.getLogger("application");
	EntityManager sql;

	PetAccessor(EntityManager hibernateHelper){
		sql = hibernateHelper;
	}

	public List<Pet> getPetsByUserID(int id){
		List<Pet> pets = null;
		try {
			pets = sql.createQuery("from Pet where ownerID = :id").setParameter("id", id).getResultList();
		} catch (PersistenceException ex) {
			logger.error("No pets found for user " + id, ex);
		}
		return pets;
	}

	public Pet getPetByID(int id){
		Pet pet = null;
		try {
			pet = (Pet) sql.createQuery("from Pet where petID = :id").setParameter("id", id).getSingleResult();
		} catch (PersistenceException ex) {
			logger.error("No pet found for pet " + id, ex);
		}
		return pet;
	}

	public boolean isPetNameTaken(String petName){
		try {
			return !(boolean) sql.createNativeQuery("SELECT EXISTS(SELECT 1 from pets where petName = ?1)")
											.setParameter(1, petName)
											.getSingleResult();
		} catch(Exception e) {
			logger.error("This pet name so shit it blew up the server? " + petName, e);
			return true;
		}
	}

	public void save(List<Pet> pets) {
		pets.forEach(p -> save(p));
	}

	public void save(Pet pet) {
		sql.merge(pet);
	}
}
