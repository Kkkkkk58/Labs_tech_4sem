package ru.kslacker.cats.services;

import org.hibernate.Transaction;
import ru.kslacker.cats.dataaccess.dao.api.CatDao;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.models.Breed;
import ru.kslacker.cats.dataaccess.models.FurColor;
import ru.kslacker.cats.dataaccess.utils.DbService;
import ru.kslacker.cats.services.api.CatService;
import java.util.List;
import java.util.function.Predicate;

public class CatServiceImpl implements CatService {

	private final CatDao catDao;

	public CatServiceImpl(CatDao catDao) {
		this.catDao = catDao;
	}

	@Override
	public Cat addCat(Cat cat) {
		Transaction transaction = DbService.getTransaction();
		Cat ret = catDao.create(cat);
		transaction.commit();
		return ret;
	}

	@Override
	public void removeCat(Cat cat) {
		Transaction transaction = DbService.getTransaction();
		catDao.delete(cat);
		transaction.commit();
	}

	@Override
	public Cat getCat(Long id) {
		return catDao.getById(id);
	}

	@Override
	public void updateCat(Cat cat) {
		Transaction transaction = DbService.getTransaction();
		catDao.update(cat);
		transaction.commit();
	}

	@Override
	public List<Cat> getByOwner(CatOwner owner) {
		return getBy(cat -> cat.getOwner().equals(owner));
	}

	@Override
	public List<Cat> getByColor(FurColor color) {
		return getBy(cat -> cat.getFurColor().equals(color));
	}

	@Override
	public List<Cat> getByBreed(Breed breed) {
		return getBy(cat -> cat.getBreed().equals(breed));
	}

	@Override
	public List<Cat> getBy(Predicate<Cat> condition) {
		return catDao.getAll().stream().filter(condition).toList();
	}
}
