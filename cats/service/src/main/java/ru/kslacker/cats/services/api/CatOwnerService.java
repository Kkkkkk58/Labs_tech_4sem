package ru.kslacker.cats.services.api;

import java.util.List;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.models.catowners.CatOwnerInformation;
import ru.kslacker.cats.services.models.catowners.CatOwnerSearchOptions;
import ru.kslacker.cats.services.models.catowners.CatOwnerUpdateInformation;

public interface CatOwnerService {

	CatOwnerDto create(CatOwnerInformation catOwnerInformation);

	void delete(Long id);

	CatOwnerDto get(Long id);

	List<CatOwnerDto> getBy(CatOwnerSearchOptions searchOptions);

	boolean exists(Long id);

	CatOwnerDto update(CatOwnerUpdateInformation catOwnerUpdateInformation);
}
