package ru.kslacker.cats.dataaccess.dao;

import jakarta.persistence.EntityManager;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.dao.api.AbstractDao;
import ru.kslacker.cats.dataaccess.dao.api.CatDao;
import ru.kslacker.cats.dataaccess.entities.Cat;
import java.time.LocalDate;
import java.util.List;

public class CatDaoImpl extends AbstractDao<Cat, Long> implements CatDao {

	public CatDaoImpl(EntityManager entityManager) {
		super(Cat.class, entityManager);
	}

	@Override
	public List<Cat> getByName(String name) {
		return getByParam("name", name);
	}

	@Override
	public List<Cat> getByDateOfBirth(LocalDate dateOfBirth) {
		return getByParam("dateOfBirth", dateOfBirth);
	}

	@Override
	public List<Cat> getByOwner(Long ownerId) {
		return getByParam("owner_id", ownerId);
	}

	@Override
	public List<Cat> getByColor(FurColor color) {
		return getByParam("fur_color", color);
	}

	@Override
	public List<Cat> getByBreed(String breed) {
		return getByParam("breed", breed);
	}
}
