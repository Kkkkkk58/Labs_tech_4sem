package ru.kslacker.cats.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.mapping.CatOwnerExtensions;
import ru.kslacker.cats.services.mapping.StreamExtensions;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@ExtensionMethod({CatOwnerExtensions.class, StreamExtensions.class})
public class CatOwnerServiceImpl implements CatOwnerService {

	private final EntityManager entityManager;
	private final CatOwnerDao catOwnerDao;

	public CatOwnerServiceImpl(@NonNull EntityManager entityManager,
		@NonNull CatOwnerDao catOwnerDao) {
		this.entityManager = entityManager;
		this.catOwnerDao = catOwnerDao;
	}

	@Override
	public CatOwnerDto create(@NonNull String name, @NonNull LocalDate dateOfBirth) {

		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			CatOwner owner = new CatOwner(name, dateOfBirth);
			CatOwnerDto saved = catOwnerDao.save(owner).asDto();
			transaction.commit();
			return saved;
		} catch (PersistenceException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void remove(@NonNull Long id) {

		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			CatOwner owner = catOwnerDao.getById(id);
			catOwnerDao.delete(owner);
			transaction.commit();
		} catch (PersistenceException e) {
			transaction.rollback();
			throw e;
		}

	}

	@Override
	public CatOwnerDto get(@NonNull Long id) {
		return catOwnerDao.getById(id).asDto();
	}

	@Override
	public List<CatOwnerDto> getByName(@NonNull String name) {
		return catOwnerDao.getByName(name).stream().asCatOwnerDto().toList();
	}

	@Override
	public List<CatOwnerDto> getByDateOfBirth(@NonNull LocalDate dateOfBirth) {
		return catOwnerDao.getByDateOfBirth(dateOfBirth).stream().asCatOwnerDto().toList();
	}

	@Override
	public List<CatOwnerDto> getBy(@NonNull Predicate<CatOwner> condition) {
		return catOwnerDao.getAll().stream().filter(condition).asCatOwnerDto().toList();
	}

	@Override
	public List<CatOwnerDto> getBy(@NonNull Map<String, Object> paramSet) {
		return catOwnerDao.getByParamSet(paramSet).stream().asCatOwnerDto().toList();
	}
}
