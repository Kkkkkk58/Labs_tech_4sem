package ru.kslacker.cats.services.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.CatOwnerUpdateDto;

public interface CatOwnerService {

	CatOwnerDto create(String name, LocalDate dateOfBirth);

	void delete(Long id);

	CatOwnerDto get(Long id);

	List<CatOwnerDto> getBy(Predicate<CatOwner> condition);

	List<CatOwnerDto> getBy(Map<String, Object> paramSet);

    boolean exists(Long id);

	CatOwnerDto update(CatOwnerUpdateDto catOwnerDto);
}
