package ru.kslacker.cats.dataaccess.dao;

import org.hibernate.SessionFactory;
import ru.kslacker.cats.dataaccess.dao.api.AbstractDao;
import ru.kslacker.cats.dataaccess.dao.api.CatDao;
import ru.kslacker.cats.dataaccess.entities.Cat;

public class CatDaoImpl extends AbstractDao<Cat, Long> implements CatDao {

	public CatDaoImpl(SessionFactory sessionFactory) {
		super(Cat.class, sessionFactory);
	}
}
