package ru.kslacker.cats.dataaccess.dao;

import jakarta.persistence.EntityManager;
import ru.kslacker.cats.dataaccess.dao.api.AbstractDao;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import java.time.LocalDate;
import java.util.List;

public class CatOwnerDaoImpl extends AbstractDao<CatOwner, Long> implements CatOwnerDao {

	public CatOwnerDaoImpl(EntityManager entityManager) {
		super(CatOwner.class, entityManager);
	}

	@Override
	public List<CatOwner> getByName(String name) {
		return getByParam("name", name);
	}

	@Override
	public List<CatOwner> getByDateOfBirth(LocalDate dateOfBirth) {
		return getByParam("dateOfBirth", dateOfBirth);
	}
}
