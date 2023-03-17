package ru.kslacker.cats.presentation.controllers;

import java.util.List;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.presentation.models.cats.CreateCatModel;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;

public class CatController {

	private final CatService service;

	public CatController(CatService service) {
		this.service = service;
	}

	public CatDto create(CreateCatModel cat) {
		// TODO WTF
		return service.create(cat.name(), cat.dateOfBirth(), cat.breed(), cat.furColor(), cat.ownerId());
	}

	public void delete(Long id) {
		service.remove(id);
	}

	public CatDto getCat(Long id) {
		return service.get(id);
	}

	public List<CatDto> getByOwner(Long ownerId) {
		return service.getByOwner(ownerId);
	}

	public List<CatDto> getByColor(FurColor color) {
		return service.getByColor(color);
	}

	public List<CatDto> getByBreed(String breed) {
		return service.getByBreed(breed);
	}
}
