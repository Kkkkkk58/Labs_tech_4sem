package ru.kslacker.cats.dataaccess.dao.api;

import ru.kslacker.cats.dataaccess.entities.Cat;
import java.util.List;

public interface CatDao {

	Cat create(Cat cat);
	Cat getById(Long id);
	List<Cat> getAll();
	void update(Cat cat);
	void delete(Cat cat);
}
