package ru.kslacker.cats.services.api;

import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.dto.CatOwnerDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface CatOwnerService {

	CatOwnerDto create(String name, LocalDate dateOfBirth);

	void delete(Long id);

	CatOwnerDto get(Long id);

	List<CatOwnerDto> getBy(Predicate<CatOwner> condition);

	List<CatOwnerDto> getBy(Map<String, Object> paramSet);
}
