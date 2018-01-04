package com.critters.bll;

import com.critters.dal.DAL;
import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Jeremy on 8/28/2016.
 */
public class PetBLL {

	static final Logger logger = LoggerFactory.getLogger("application");

	public static Pet createPet(Pet pet, User owner){
		if(pet.getPetID() != 0) return null;
		//validate()
		pet.setOwnerID(owner.getUserID());
		try (DAL dal = new DAL()){
			dal.beginTransaction();
			dal.pets.save(pet);
			if(!dal.commitTransaction()) {
				logger.error("Pet creation failed for pet " + pet.toString());
				pet = null;
			}
		}
		return pet;
	}

	public static List<Pet> getPets(int userID) {
		try (DAL dal = new DAL()) {
			return dal.pets.getPetsByUserID(userID);
		}
	}

	public static void updatePet(Pet pet){
		//todo validate
		try (DAL dal = new DAL()) {
			dal.pets.getPetByID(pet.getPetID());
			dal.beginTransaction();
			dal.pets.save(pet);
			if(!dal.commitTransaction()) {
				logger.error("Pet update failed for pet " + pet.toString());
			}
		}
	}

	public static List<Pet.PetSpecies> getPetSpecies() {
		try(DAL dal = new DAL()){
			return dal.configuration.getPetSpecies();
		}
	}

	public static List<Pet.PetColor> getPetColors() {
		try(DAL dal = new DAL()){
			return dal.configuration.getPetColors();
		}
	}

	public static boolean isPetNameValid(String petName){
		try(DAL dal = new DAL()) {
			return dal.pets.isPetNameTaken(petName);
		}
	}

}
