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

	protected static SessionFactory getSessionFactory() {
		tryToConnectToSQL();
		return sessionFactory;
	}

	protected static EntityManagerFactory getEntityManagerFactory() {
		tryToConnectToSQL();
		return entityManagerFactory;
	}
}