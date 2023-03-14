package ru.kslacker.cats.services;

import org.hibernate.Transaction;
import ru.kslacker.cats.dataaccess.dao.api.CatOwnerDao;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.utils.DbService;
import ru.kslacker.cats.services.api.CatOwnerService;
import java.util.List;
import java.util.function.Predicate;

public class CatOwnerServiceImpl implements CatOwnerService {

	private final CatOwnerDao catOwnerDao;

	public CatOwnerServiceImpl(CatOwnerDao catOwnerDao) {
		this.catOwnerDao = catOwnerDao;
	}

	@Override
	public CatOwner addOwner(CatOwner owner) {
		Transaction transaction = DbService.getTransaction();
		CatOwner saved = catOwnerDao.create(owner);
		transaction.commit();
		return saved;
	}

	@Override
	public void removeOwner(CatOwner owner) {
		Transaction transaction = DbService.getTransaction();
		catOwnerDao.delete(owner);
		transaction.commit();
	}

	@Override
	public CatOwner getOwner(Long id) {
		return catOwnerDao.getById(id);
	}

	@Override
	public void updateOwner(CatOwner owner) {
		Transaction transaction = DbService.getTransaction();
		catOwnerDao.update(owner);
		transaction.commit();
	}

	@Override
	public List<CatOwner> getBy(Predicate<CatOwner> condition) {
		return catOwnerDao.getAll().stream().filter(condition).toList();
	}
}
