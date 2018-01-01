package com.critters.dal;

import com.critters.bll.PetBLL;
import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;

import javax.persistence.EntityManager;

/**
 * Created by Jeremy on 12/31/2017.
 */
public class UserDAL {

	private EntityManager entityManager;

	public UserDAL(){
		this.entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
	}

	public UserDAL(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	private void getTrxn() {
		this.entityManager.getTransaction().begin();
	}

	private void endTrxn() {
		if(entityManager.getTransaction().isActive()){
			entityManager.getTransaction().rollback();
		}
	}

	private void done(){
		endTrxn();
		entityManager.close();
	}

	public void updateUserWithTransaction(User changeUser, boolean updateSecurityFields){
		try {
			getTrxn();
			updateUser(changeUser, updateSecurityFields);
		} finally {
			endTrxn();
		}
	}

	protected User getUser(int userID){
		User user = (User) entityManager.createQuery("from User where userID = :id and isActive = true").setParameter("id", userID).getSingleResult();
		return user;
	}

	protected void updateUser(User changeUser, boolean updateSecurityFields) { //todo consider iftesting each set to circumvent hibernate dirtycheck
		User dbUser = getUser(changeUser.getUserID());
		dbUser.setBirthDay(changeUser.getBirthDay());
		dbUser.setBirthMonth(changeUser.getBirthMonth());
		dbUser.setCity(changeUser.getCity());
		dbUser.setCountry(changeUser.getCity());
		dbUser.setCritterbuxx(changeUser.getCritterbuxx());
		dbUser.setEmailAddress(changeUser.getEmailAddress());
		dbUser.setFirstName(changeUser.getFirstName());
		dbUser.setLastName(changeUser.getLastName());
		dbUser.setPostcode(changeUser.getPostcode());
		dbUser.setSex(changeUser.getSex());
		dbUser.setState(changeUser.getState());
		dbUser.setUserImagePath(changeUser.getUserImagePath());
		dbUser.setUserName(changeUser.getUserName());
		if(updateSecurityFields) {
			dbUser.setPassword(changeUser.getPassword());
			dbUser.setSalt(changeUser.getSalt());
			dbUser.setTokenSelector(changeUser.getTokenSelector());
			dbUser.setTokenValidator(changeUser.getTokenValidator());
		}
		entityManager.persist(dbUser);
	}

	protected void deleteUser(int userID){
		User user = getUser(userID);
		user.setIsActive(false);
		for (Pet pet : user.getPets()) {
			PetBLL.abandonPet(pet);
		}
	}

}
