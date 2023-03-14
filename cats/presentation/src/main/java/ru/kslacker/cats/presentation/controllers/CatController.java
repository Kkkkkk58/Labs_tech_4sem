package ru.kslacker.cats.presentation.controllers;

import java.util.List;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.models.Breed;
import ru.kslacker.cats.dataaccess.models.FurColor;
import ru.kslacker.cats.presentation.dto.CatDto;
import ru.kslacker.cats.presentation.utils.mapping.CatExtensions;
import ru.kslacker.cats.services.api.CatService;

@ExtensionMethod(CatExtensions.class)
public class CatController {

	private final CatService service;

	public CatController(CatService service) {
		this.service = service;
	}

	public CatDto create(Cat cat) {
		// TODO WTF
		return service.addCat(cat).asDto();
	}

	public void delete(Cat cat) {
		service.removeCat(cat);
	}

	public CatDto getCat(Long id) {
		return service.getCat(id).asDto();
	}

	public List<CatDto> getByOwner(CatOwner owner) {
		return service.getByOwner(owner).stream().map(c -> c.asDto()).toList();
	}

	public List<CatDto> getByColor(FurColor color) {
		return service.getByColor(color).stream().map(c -> c.asDto()).toList();
	}

	public List<CatDto> getByBreed(Breed breed) {
		return service.getByBreed(breed).stream().map(c -> c.asDto()).toList();
	}

	public void update(Cat cat) {
		service.updateCat(cat);
	}
}
