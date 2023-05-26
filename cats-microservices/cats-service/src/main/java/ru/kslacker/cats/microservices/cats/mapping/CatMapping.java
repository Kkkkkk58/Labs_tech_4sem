package ru.kslacker.cats.microservices.cats.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.microservices.cats.dto.CatDto;
import ru.kslacker.cats.microservices.jpaentities.entities.Cat;
import java.util.stream.Stream;

@UtilityClass
public class CatMapping {

	public static CatDto asDto(Cat cat) {
		return CatDto.builder()
			.id(cat.getId())
			.name(cat.getName())
			.dateOfBirth(cat.getDateOfBirth())
			.breed(cat.getBreed())
			.furColor(cat.getFurColor())
			.ownerId(cat.getOwnerId())
			.friends(cat.getFriends().stream().map(Cat::getId).toList())
			.build();
	}

	public static Stream<CatDto> asCatDto(Stream<Cat> cats) {
		return cats.map(CatMapping::asDto);
	}

}
