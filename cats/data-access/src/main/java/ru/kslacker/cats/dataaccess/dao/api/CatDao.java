package ru.kslacker.cats.dataaccess.dao.api;

import java.time.LocalDate;
import java.util.List;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.entities.Cat;

public interface CatDao extends Dao<Cat, Long> {

	List<Cat> getByName(String name);
	List<Cat> getByDateOfBirth(LocalDate dateOfBirth);
	List<Cat> getByOwner(Long ownerId);
	List<Cat> getByColor(FurColor color);
	List<Cat> getByBreed(String breed);
}
