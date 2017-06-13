package com.critters.dal;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Jeremy on 8/7/2016.
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;
	private static final EntityManagerFactory entityManagerFactory;
	static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
			entityManagerFactory = Persistence.createEntityManagerFactory("com.critters");
		} catch (Throwable ex) {
			logger.error("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
}