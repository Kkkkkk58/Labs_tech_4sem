package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.services.dto.CatDto;

@UtilityClass
public class CatMapping {

	public static CatDto asDto(Cat cat) {
		return CatDto.builder()
			.id(cat.getId())
			.name(cat.getName())
			.dateOfBirth(cat.getDateOfBirth())
			.breed(cat.getBreed())
			.furColor(cat.getFurColor())
			.ownerId(cat.getOwner().getId())
			.friends(cat.getFriends().stream().map(Cat::getId).toList())
			.build();
	}
}
