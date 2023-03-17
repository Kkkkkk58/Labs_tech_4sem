package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import java.util.stream.Stream;

@UtilityClass
public class StreamExtensions {

	public static Stream<CatDto> asCatDto(Stream<Cat> cats) {
		return cats.map(CatExtensions::asDto);
	}

	public static Stream<CatOwnerDto> asCatOwnerDto(Stream<CatOwner> catOwners) {
		return catOwners.map(CatOwnerExtensions::asDto);
	}
}
