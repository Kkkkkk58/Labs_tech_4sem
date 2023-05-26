package ru.kslacker.cats.microservices.catowners.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.microservices.catowners.dto.CatOwnerDto;
import ru.kslacker.cats.microservices.jpaentities.entities.Cat;
import ru.kslacker.cats.microservices.jpaentities.entities.CatOwner;
import java.util.stream.Stream;

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

	public static Stream<CatOwnerDto> asCatOwnerDto(Stream<CatOwner> catOwners) {
		return catOwners.map(CatOwnerMapping::asDto);
	}
}
