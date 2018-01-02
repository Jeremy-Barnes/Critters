package com.critters.dal;

import javax.persistence.EntityManager;

/**
 * Created by Jeremy on 12/31/2017.
 */
public abstract class DAL {

	protected EntityManager entityManager;

	public DAL(){
		this.entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
	}

	public DAL(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	protected void getTrxn() {
		this.entityManager.getTransaction().begin();
	}

	protected void endTrxn() {
		if(entityManager.getTransaction().isActive()){
			entityManager.getTransaction().rollback();
		}
	}

	public void done(){
		endTrxn();
		entityManager.close();
	}
}
