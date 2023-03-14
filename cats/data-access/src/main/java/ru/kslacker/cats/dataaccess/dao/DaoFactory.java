package ru.kslacker.cats.dataaccess.dao;

import org.hibernate.SessionFactory;
import ru.kslacker.cats.dataaccess.dao.api.CatDao;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.dataaccess.utils.DbService;


public class DaoFactory {

	private static final SessionFactory sessionFactory;

	static {
		sessionFactory = DbService.getSessionFactory();
	}

	private static final class CatDaoHolder {

		private static final CatDao catDao = new CatDaoImpl(sessionFactory);
	}

	private static final class CatDaoOwnerHolder {

		private static final CatOwnerDao catOwnerDao = new CatOwnerDaoImpl(sessionFactory);
	}

	public static CatDao getCatDao() {
		return CatDaoHolder.catDao;
	}

	public static CatOwnerDao getCatOwnerDao() {
		return CatDaoOwnerHolder.catOwnerDao;
	}

}
