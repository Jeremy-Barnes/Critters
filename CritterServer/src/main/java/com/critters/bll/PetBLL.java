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
		List<Pet> pets = entityManager.createQuery("from Pet where ownerID = :id").setParameter("id", user.getUserID()).getResultList();
		entityManager.close();
		return pets;
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
		List<Pet.PetSpecies> options = entityManager.createQuery("from Pet$PetSpecies").getResultList();
		entityManager.close();
		return options;
	}

	public static List<Pet.PetColor> getPetColors() {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		List<Pet.PetColor> options = entityManager.createQuery("from Pet$PetColor").getResultList();
		entityManager.close();
		return options;
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
			valid = !(boolean) entityManager.createNativeQuery("SELECT EXISTS(SELECT 1 from pets where petName = ?1)")
													.setParameter(1, petName)
													.getSingleResult();
			entityManager.close();
		}
		return valid;
	}

}
