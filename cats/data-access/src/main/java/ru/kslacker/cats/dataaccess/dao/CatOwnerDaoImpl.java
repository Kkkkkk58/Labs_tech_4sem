package ru.kslacker.cats.dataaccess.dao;

import org.hibernate.SessionFactory;
import ru.kslacker.cats.dataaccess.dao.api.AbstractDao;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.dataaccess.entities.CatOwner;

public class CatOwnerDaoImpl extends AbstractDao<CatOwner, Long> implements CatOwnerDao {

	public CatOwnerDaoImpl(SessionFactory sessionFactory) {
		super(CatOwner.class, sessionFactory);
	}
}
