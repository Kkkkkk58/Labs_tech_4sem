package ru.kslacker.cats.presentation.controllers;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.presentation.models.cats.CreateCatModel;
import ru.kslacker.cats.presentation.models.cats.GetCatByParamsModel;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;

public class CatController {

	private final CatService service;

	public CatController(CatService service) {
		this.service = service;
	}

	public CatDto create(CreateCatModel cat) {
		return service.create(cat.name(), cat.dateOfBirth(), cat.breed(), cat.furColor(), cat.ownerId());
	}

	public void delete(Long id) {
		service.remove(id);
	}

	public CatDto get(Long id) {
		return service.get(id);
	}
	public List<CatDto> getByName(String name) {
		return service.getByName(name);
	}

	public List<CatDto> getByDateOfBirth(LocalDate dateOfBirth) {
		return service.getByDateOfBirth(dateOfBirth);
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

	public List<CatDto> getBy(GetCatByParamsModel params) {
		// TODO reduce duplication
		Map<String, Object> paramsMap = new HashMap<>();

		for (Field field : params.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				Object value = field.get(params);
				if (value != null) {
					paramsMap.put(field.getName(), value);
				}
			} catch (IllegalAccessException e) {
				// TODO exception
				throw new RuntimeException(e);
			}
		}

		return service.getBy(paramsMap);
	}

	public void makeFriends(Long cat1Id, Long cat2Id) {
		service.makeFriends(cat1Id, cat2Id);
	}

	public void removeFriend(Long cat1Id, Long cat2Id) {
		service.removeFriend(cat1Id, cat2Id);
	}
}
