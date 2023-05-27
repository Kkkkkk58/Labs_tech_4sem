package ru.kslacker.cats.microservices.catowners.services.api;

import ru.kslacker.cats.microservices.catowners.dto.CatOwnerDto;
import ru.kslacker.cats.microservices.catowners.dto.CatOwnerInformation;
import ru.kslacker.cats.microservices.catowners.dto.CatOwnerSearchOptions;
import ru.kslacker.cats.microservices.catowners.dto.CatOwnerUpdateInformation;
import java.util.List;

public interface CatOwnerService {

	CatOwnerDto create(CatOwnerInformation catOwnerInformation);

	boolean delete(Long id);

	CatOwnerDto get(Long id);

	List<CatOwnerDto> getBy(CatOwnerSearchOptions searchOptions);

	boolean exists(Long id);

	CatOwnerDto update(CatOwnerUpdateInformation catOwnerUpdateInformation);
}
