package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.dto.CatOwnerDto;

@UtilityClass
public class CatOwnerMapping {

	public static CatOwnerDto asDto(CatOwner catOwner) {
		return CatOwnerDto.builder()
			.id(catOwner.getId())
			.name(catOwner.getName())
			.dateOfBirth(catOwner.getDateOfBirth())
			.cats(catOwner.getCats().stream().map(Cat::getId).toList())
			.build();
	}
}
