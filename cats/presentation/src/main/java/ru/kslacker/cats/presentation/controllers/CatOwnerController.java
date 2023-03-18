package ru.kslacker.cats.presentation.controllers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.cats.presentation.models.catowners.CreateCatOwnerModel;
import ru.kslacker.cats.presentation.models.catowners.GetCatOwnerByParamsModel;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.mapping.CatOwnerExtensions;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public List<CatOwnerDto> getByName(String name) {
		return service.getByName(name);
	}

	public List<CatOwnerDto> getByDateOfBirth(LocalDate dateOfBirth) {
		return service.getByDateOfBirth(dateOfBirth);
	}

	public List<CatOwnerDto> getBy(GetCatOwnerByParamsModel params) {

		Map<String, Object> paramsMap = new HashMap<>();

		for (Field field : params.getClass().getFields()) {
			try {
				Object value = field.get(params);
				if (value != null) {
					paramsMap.put(field.getName(), value);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return service.getBy(paramsMap);
	}
}
