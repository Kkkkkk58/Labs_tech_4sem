package ru.kslacker.cats.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.dao.api.CatDao;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.mapping.CatExtensions;
import ru.kslacker.cats.services.mapping.StreamExtensions;

// TODO Transactions to services
@ExtensionMethod({ CatExtensions.class, StreamExtensions.class })
public class CatServiceImpl implements CatService {

	private final EntityManager entityManager;
	private final CatDao catDao;
	private final CatOwnerDao catOwnerDao;

	public CatServiceImpl(EntityManager entityManager, CatDao catDao, CatOwnerDao catOwnerDao) {

		this.entityManager = entityManager;
		this.catDao = catDao;
		this.catOwnerDao = catOwnerDao;
	}

	@Override
	public CatDto create(
		String name,
		LocalDate dateOfBirth,
		String breed,
		FurColor furColor,
		Long catOwnerId) {

		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			CatOwner catOwner = catOwnerDao.getById(catOwnerId);
			Cat cat = new Cat(name, dateOfBirth, breed, furColor, catOwner);
			CatDto catDto = catDao.save(cat).asDto();
			transaction.commit();
			return catDto;
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
			Cat cat = catDao.getById(id);
			catDao.delete(cat);
			transaction.commit();
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new RuntimeException();
		}
	}

	@Override
	public CatDto get(Long id) {
		return catDao.getById(id).asDto();
	}

	@Override
	public List<CatDto> getByName(String name) {
		return catDao.getByName(name).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getByDateOfBirth(LocalDate dateOfBirth) {
		return catDao.getByDateOfBirth(dateOfBirth).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getByOwner(Long ownerId) {
		return catDao.getByOwner(ownerId).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getByColor(FurColor color) {
		return catDao.getByColor(color).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getByBreed(String breed) {
		return catDao.getByBreed(breed).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getBy(Predicate<Cat> condition) {
		return catDao.getAll().stream().filter(condition).asCatDto().toList();
	}

	@Override
	public List<CatDto> getBy(Map<String, Object> paramSet) {
		return catDao.getByParamSet(paramSet).stream().asCatDto().toList();
	}

	@Override
	public void makeFriends(Long cat1Id, Long cat2Id) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Cat cat1 = catDao.getById(cat1Id);
			Cat cat2 = catDao.getById(cat2Id);

			cat1.addFriend(cat2);
			catDao.update(cat1);
			transaction.commit();
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new RuntimeException();
		}
	}

	@Override
	public void removeFriends(Long cat1Id, Long cat2Id) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Cat cat1 = catDao.getById(cat1Id);
			Cat cat2 = catDao.getById(cat2Id);

			cat1.removeFriend(cat2);
			catDao.update(cat1);
			transaction.commit();
		} catch (PersistenceException e) {
			transaction.rollback();
			throw new RuntimeException();
		}
	}
}
