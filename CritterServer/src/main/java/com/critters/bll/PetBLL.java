package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class PetBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static Pet createPet(Pet pet, User owner){
		pet.setOwnerID(owner.getUserID());
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(pet);
			entityManager.getTransaction().commit();
		}catch(Exception e) {
			logger.error("Pet creation failed for pet " + pet.toString(), e);
			throw e;
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
		return pet;
	}

	public static List<Pet> getPets(User user){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Pet> pets = entityManager.createQuery("from Pet where ownerID = :id").setParameter("id", user.getUserID()).getResultList();
			return pets;
		} catch (Exception e){
			logger.error("NO PETS FOR user " + user.getUserID(), e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	public static void updatePet(Pet pet){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			entityManager.merge(pet);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Pet update failed for pet " + pet.toString(), e);
		} finally {
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			entityManager.close();
		}
	}

	public static List<Pet.PetSpecies> getPetSpecies() {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Pet.PetSpecies> options = entityManager.createQuery("from Pet$PetSpecies").getResultList();
			return options;
		} catch(Exception e) {
			logger.error("SOMEHOW THERE ARE NO SPECIES", e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	public static List<Pet.PetColor> getPetColors() {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			List<Pet.PetColor> options = entityManager.createQuery("from Pet$PetColor").getResultList();
			return options;
		} catch(Exception e) {
			logger.error("SOMEHOW THERE ARE NO COLORS", e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	public static void abandonPet(Pet pet){
		pet.setIsAbandoned(true);
		updatePet(pet);
	}

	public static boolean isPetNameValid(String petName){
		boolean valid = true;
		valid = (petName != null && !petName.isEmpty()); //todo: content filter
		if(valid) {
			EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
			try {
				valid = !(boolean) entityManager.createNativeQuery("SELECT EXISTS(SELECT 1 from pets where petName = ?1)")
												.setParameter(1, petName)
												.getSingleResult();
			} catch(Exception e) {
				logger.error("This pet name so shit it blew up the server? " + petName, e);
			}
			entityManager.close();
		}
		return valid;
	}

}
