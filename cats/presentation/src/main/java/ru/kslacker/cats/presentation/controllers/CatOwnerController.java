package ru.kslacker.cats.presentation.controllers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.cats.presentation.models.catowners.CreateCatOwnerModel;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.mapping.CatOwnerExtensions;

@ExtensionMethod(CatOwnerExtensions.class)
public class CatOwnerController {

	private final CatOwnerService service;

	public CatOwnerController(CatOwnerService service) {
		this.service = service;
	}

	public CatOwnerDto create(CreateCatOwnerModel owner) {
		return service.create(owner.name(), owner.dateOfBirth());
	}

	public CatOwnerDto get(Long id) {
		return service.get(id);
	}

	public void delete(Long id) {
		service.remove(id);
	}
}
