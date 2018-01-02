package com.critters.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Jeremy on 1/1/2018.
 */
public class OberDAL implements AutoCloseable {
	static final Logger logger = LoggerFactory.getLogger("application");
	public HibernateUtil.HibernateHelper sql;
	public ItemAccessor items;
	public UserAccessor users;


	public OberDAL(HibernateUtil.HibernateHelper hibernateHelper){
		sql = hibernateHelper;
		items = new ItemAccessor(hibernateHelper);
		users = new UserAccessor(hibernateHelper);
	}

	@Override
	public void close(){
		sql.close();
	}

	public static class TransactionHelper implements AutoCloseable {
		HibernateUtil.HibernateHelper sql;
		public TransactionHelper(HibernateUtil.HibernateHelper sql) {
			this.sql = sql;
			sql.beginTransaction();
		}

		@Override
		public void close(){
			sql.commitTransaction();
		}
	}

}
