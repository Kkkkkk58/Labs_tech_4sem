package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.User;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.UserDto;
import java.util.stream.Stream;

@UtilityClass
public class StreamMapping {

	public static Stream<CatDto> asCatDto(Stream<Cat> cats) {
		return cats.map(CatMapping::asDto);
	}

	public static Stream<CatOwnerDto> asCatOwnerDto(Stream<CatOwner> catOwners) {
		return catOwners.map(CatOwnerMapping::asDto);
	}

	public static Stream<UserDto> asUserDto(Stream<User> users) {
		return users.map(UserMapping::asDto);
	}
}
