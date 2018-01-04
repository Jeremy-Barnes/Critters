package com.critters.dal;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Created by Jeremy on 8/7/2016.
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static EntityManagerFactory entityManagerFactory;
	static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

	static {
		try {
			tryToConnectToSQL();
		} catch (ExceptionInInitializerError ex) {
			logger.error("Initial SQL Connection failed." + ex);
		}
	}

	private static void tryToConnectToSQL() {
		try {
			if(sessionFactory == null) sessionFactory = new Configuration().configure().buildSessionFactory();
			if(entityManagerFactory == null) entityManagerFactory = Persistence.createEntityManagerFactory("com.critters");
		} catch (Throwable ex) {
			logger.error("SessionFactory/EntityManagerFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		tryToConnectToSQL();
		return sessionFactory;
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		tryToConnectToSQL();
		return entityManagerFactory;
	}

//	public static class HibernateHelper implements AutoCloseable {
//		protected EntityManager entityManager;
//		public HibernateHelper(){
//			entityManager = HibernateUtil.entityManagerFactory.createEntityManager();
//		}
//
//		public void beginTransaction(){
//			if(!entityManager.getTransaction().isActive())
//				entityManager.getTransaction().begin();
//		}
//
//		public boolean commitTransaction(){
//			try {
//				if (entityManager.getTransaction().isActive()) {
//					entityManager.getTransaction().commit();
//					return true;
//				}
//			} catch(Exception e) {
//				rollback();
//				logger.error("Transaction failed", e);
//			}
//			return false;
//		}
//
//		public void rollback(){
//			if (entityManager.getTransaction().isActive())
//				entityManager.getTransaction().rollback();
//		}
//
//		public Query createQuery(String query) {
//			return entityManager.createQuery(query);
//		}
//
//		public Query createNativeQuery(String query){
//			return entityManager.createNativeQuery(query);
//		}
//
//		public StoredProcedureQuery createStoredProcedureQuery(String query, Class returnType) {
//			return entityManager.createStoredProcedureQuery(query, returnType);
//		}
//
//		@Override
//		public void close() {
//			rollback();
//			entityManager.close();
//		}
	//}
}