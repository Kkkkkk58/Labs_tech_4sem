package ru.kslacker.cats.presentation.utils.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.presentation.dto.CatDto;

@UtilityClass
public class CatExtensions {

	public static CatDto asDto(Cat cat) {
		return new CatDto(
			cat.getId(),
			cat.getName(),
			cat.getDateOfBirth(),
			cat.getBreed(),
			cat.getFurColor(),
			cat.getOwner(),
			cat.getFriends().stream().map(CatExtensions::asDto).toList());
	}
}
