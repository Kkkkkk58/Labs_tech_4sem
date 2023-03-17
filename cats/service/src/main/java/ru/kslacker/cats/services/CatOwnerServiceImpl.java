package ru.kslacker.cats.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.mapping.CatOwnerExtensions;
import ru.kslacker.cats.services.mapping.StreamExtensions;

@ExtensionMethod({ CatOwnerExtensions.class, StreamExtensions.class })
public class CatOwnerServiceImpl implements CatOwnerService {

	private final EntityManager entityManager;
	private final CatOwnerDao catOwnerDao;

	public CatOwnerServiceImpl(EntityManager entityManager, CatOwnerDao catOwnerDao) {
		this.entityManager = entityManager;
		this.catOwnerDao = catOwnerDao;
	}

	@Override
	public CatOwnerDto create(String name, LocalDate dateOfBirth) {

		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			CatOwner owner = new CatOwner(name, dateOfBirth);
			CatOwnerDto catOwnerDto = catOwnerDao.save(owner).asDto();
			transaction.commit();
			return catOwnerDto;
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new RuntimeException();
		}
	}

	@Override
	public void remove(Long id) {

		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			CatOwner owner = catOwnerDao.getById(id);
			catOwnerDao.delete(owner);
			transaction.commit();
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new RuntimeException();
		}
	}

	@Override
	public CatOwnerDto get(Long id) {
		return catOwnerDao.getById(id).asDto();
	}

	@Override
	public List<CatOwnerDto> getByName(String name) {
		return catOwnerDao.getByName(name).stream().asCatOwnerDto().toList();
	}

	@Override
	public List<CatOwnerDto> getByDateOfBirth(LocalDate dateOfBirth) {
		return catOwnerDao.getByDateOfBirth(dateOfBirth).stream().asCatOwnerDto().toList();
	}

	@Override
	public List<CatOwnerDto> getBy(Predicate<CatOwner> condition) {
		return catOwnerDao.getAll().stream().filter(condition).asCatOwnerDto().toList();
	}

	@Override
	public List<CatOwnerDto> getBy(Map<String, Object> paramSet) {
		return catOwnerDao.getByParamSet(paramSet).stream().asCatOwnerDto().toList();
	}
}
