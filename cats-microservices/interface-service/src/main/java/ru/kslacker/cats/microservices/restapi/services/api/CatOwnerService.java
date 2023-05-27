package ru.kslacker.cats.microservices.restapi.services.api;

import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerUpdateInformation;
import java.util.List;

public interface CatOwnerService {

	CatOwnerDto create(CatOwnerInformation catOwnerInformation);

	void delete(Long id);

	CatOwnerDto get(Long id);

	List<CatOwnerDto> getBy(CatOwnerSearchOptions searchOptions);

	CatOwnerDto update(CatOwnerUpdateInformation catOwnerUpdateInformation);
}
