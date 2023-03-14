package ru.kslacker.cats.dataaccess.dao.api;

import java.util.List;
import ru.kslacker.cats.dataaccess.entities.CatOwner;

public interface CatOwnerDao {

	CatOwner create(CatOwner catOwner);
	CatOwner getById(Long id);
	List<CatOwner> getAll();
	void update(CatOwner cat);
	void delete(CatOwner cat);
}
