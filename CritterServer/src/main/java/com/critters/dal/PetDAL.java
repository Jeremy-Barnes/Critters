package com.critters.dal;

import com.critters.bll.PetBLL;
import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;

/**
 * Created by Jeremy on 12/31/2017.
 */
public class PetDAL extends DAL {

	public static void updatePet(Pet pet){
		try {
			pet.setSex(pet);
			entityManager.merge(pet);
			entityManager.getTransaction().commit();
		}
	}


	protected void deleteUser(int userID){
		User user = getUser(userID);
		user.setIsActive(false);
		for (Pet pet : user.getPets()) {
			PetBLL.abandonPet(pet);
		}
	}

}
