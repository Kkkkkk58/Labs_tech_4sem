package ru.kslacker.cats.dataaccess.dao.api;

import ru.kslacker.cats.dataaccess.entities.CatOwner;

import java.time.LocalDate;
import java.util.List;

public interface CatOwnerDao extends Dao<CatOwner, Long> {

	List<CatOwner> getByName(String name);

	List<CatOwner> getByDateOfBirth(LocalDate dateOfBirth);
}
