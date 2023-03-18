package ru.kslacker.cats.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import lombok.NonNull;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@ExtensionMethod({CatExtensions.class, StreamExtensions.class})
public class CatServiceImpl implements CatService {

	private final EntityManager entityManager;
	private final CatDao catDao;
	private final CatOwnerDao catOwnerDao;

	public CatServiceImpl(@NonNull EntityManager entityManager, @NonNull CatDao catDao,
		@NonNull CatOwnerDao catOwnerDao) {

		this.entityManager = entityManager;
		this.catDao = catDao;
		this.catOwnerDao = catOwnerDao;
	}

	@Override
	public CatDto create(
		@NonNull String name,
		@NonNull LocalDate dateOfBirth,
		@NonNull String breed,
		@NonNull FurColor furColor,
		@NonNull Long catOwnerId) {

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
			throw e;
		}
	}

	@Override
	public void remove(@NonNull Long id) {

		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			Cat cat = catDao.getById(id);
			List<Cat> friends = cat.getFriends().stream().toList();
			for (Cat friend : friends) {
				friend.removeFriend(cat);
			}
			cat.getOwner().removeCat(cat);

			catDao.delete(cat);
			transaction.commit();
		} catch (PersistenceException e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public CatDto get(@NonNull Long id) {
		return catDao.getById(id).asDto();
	}

	@Override
	public List<CatDto> getByName(@NonNull String name) {
		return catDao.getByName(name).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getByDateOfBirth(@NonNull LocalDate dateOfBirth) {
		return catDao.getByDateOfBirth(dateOfBirth).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getByOwner(Long ownerId) {
		return catDao.getByOwner(ownerId).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getByColor(@NonNull FurColor color) {
		return catDao.getByColor(color).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getByBreed(@NonNull String breed) {
		return catDao.getByBreed(breed).stream().asCatDto().toList();
	}

	@Override
	public List<CatDto> getBy(@NonNull Predicate<Cat> condition) {
		return catDao.getAll().stream().filter(condition).asCatDto().toList();
	}

	@Override
	public List<CatDto> getBy(@NonNull Map<String, Object> paramSet) {
		return catDao.getByParamSet(paramSet).stream().asCatDto().toList();
	}

	@Override
	public void makeFriends(@NonNull Long cat1Id, @NonNull Long cat2Id) {

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
			throw e;
		}
	}

	@Override
	public void removeFriend(@NonNull Long cat1Id, @NonNull Long cat2Id) {

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
			throw e;
		}
	}
}
