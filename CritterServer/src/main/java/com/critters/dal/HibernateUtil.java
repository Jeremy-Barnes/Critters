package com.critters.dal;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Jeremy on 8/7/2016.
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;
	private static final EntityManagerFactory entityManagerFactory;

	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
			entityManagerFactory = Persistence.createEntityManagerFactory("com.critters");
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
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