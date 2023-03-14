package ru.kslacker.cats.dataaccess.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public class DbService {

	private static final SessionFactory sessionFactory;

	static {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	public static Transaction getTransaction() {
		Session session = getSessionFactory().getCurrentSession();
		Transaction transaction = session.getTransaction();
		if (transaction.getStatus() != TransactionStatus.ACTIVE) {
			transaction = session.beginTransaction();
		}

		return transaction;
	}

	public static void rollback(Transaction transaction) {
		if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
			transaction.rollback();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
