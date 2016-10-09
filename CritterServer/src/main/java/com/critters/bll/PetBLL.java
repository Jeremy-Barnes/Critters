package com.critters.bll;

import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class PetBLL {

	public static Pet createPet(Pet pet, User owner){
		pet.setOwnerid(owner.getUserID());
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist(pet);

		entityManager.getTransaction().commit();
		entityManager.close();
		return pet;
	}

	public static List<Pet> getPet(User user){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		List<Pet> pets = entityManager.createQuery("from Pet where ownerid = :id").setParameter("id", user.getUserID()).getResultList();
		entityManager.close();
		return pets;
	}

	public static void updatePet(Pet pet){
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.merge(pet);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static List<Pet.PetSpecies> getPetOptions() {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		List<Pet.PetSpecies> options = entityManager.createQuery("from PetSpecies").getResultList();
		entityManager.close();
		return options;
	}

	public static List<Pet.PetColor> getPetColors() {
		EntityManager entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
		List<Pet.PetColor> options = entityManager.createQuery("from PetColor ").getResultList();
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
			valid = (boolean) entityManager.createNativeQuery("IF EXISTS(SELECT 1 from pets where petName = ?1) SELECT 1 AS FOUND ELSE SELECT 0 AS FOUND")
													.setParameter(1, petName)
													.getSingleResult();
			entityManager.close();
		}
		return valid;
	}

}
