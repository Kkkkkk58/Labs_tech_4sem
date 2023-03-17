package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.dto.CatOwnerDto;

@UtilityClass
public class CatOwnerExtensions {

	public static CatOwnerDto asDto(CatOwner catOwner) {
		return CatOwnerDto.builder()
			.id(catOwner.getId())
			.name(catOwner.getName())
			.cats(catOwner.getCats().stream().map(CatExtensions::asDto).toList())
			.build();
	}
}
