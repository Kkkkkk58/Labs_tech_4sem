package ru.kslacker.cats.services.api;

import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.models.Breed;
import ru.kslacker.cats.dataaccess.models.FurColor;
import java.util.List;
import java.util.function.Predicate;

public interface CatService {
	Cat addCat(Cat cat);
	void removeCat(Cat cat);
	Cat getCat(Long id);
	void updateCat(Cat cat);
	List<Cat> getByOwner(CatOwner owner);
	List<Cat> getByColor(FurColor color);
	List<Cat> getByBreed(Breed breed);
	List<Cat> getBy(Predicate<Cat> condition);
}
