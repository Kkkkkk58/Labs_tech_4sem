package ru.kslacker.cats.presentation.utils.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.presentation.dto.CatOwnerDto;

@UtilityClass
public class CatOwnerExtensions {

	public static CatOwnerDto asDto(CatOwner catOwner) {
		return new CatOwnerDto(
			catOwner.getId(),
			catOwner.getName(),
			catOwner.getCats().stream().map(CatExtensions::asDto).toList());
	}
}
