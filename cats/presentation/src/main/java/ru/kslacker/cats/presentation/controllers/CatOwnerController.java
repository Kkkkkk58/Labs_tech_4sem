package ru.kslacker.cats.presentation.controllers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.presentation.dto.CatOwnerDto;
import ru.kslacker.cats.presentation.utils.mapping.CatOwnerExtensions;
import ru.kslacker.cats.services.api.CatOwnerService;

@ExtensionMethod(CatOwnerExtensions.class)
public class CatOwnerController {

	private final CatOwnerService service;

	public CatOwnerController(CatOwnerService service) {
		this.service = service;
	}

	public CatOwnerDto create(CatOwner owner) {
		return service.addOwner(owner).asDto();
	}

	public CatOwnerDto get(Long id) {
		return service.getOwner(id).asDto();
	}

	public void update(CatOwner owner) {
		service.updateOwner(owner);
	}

	public void delete(CatOwner owner) {
		service.removeOwner(owner);
	}
}
