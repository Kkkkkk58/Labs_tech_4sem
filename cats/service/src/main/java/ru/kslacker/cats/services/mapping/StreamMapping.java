package ru.kslacker.cats.services.mapping;

import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.UserAccount;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.UserDto;

@UtilityClass
public class StreamMapping {

	public static Stream<CatDto> asCatDto(Stream<Cat> cats) {
		return cats.map(CatMapping::asDto);
	}

	public static Stream<CatOwnerDto> asCatOwnerDto(Stream<CatOwner> catOwners) {
		return catOwners.map(CatOwnerMapping::asDto);
	}

	public static Stream<UserDto> asUserDto(Stream<UserAccount> users) {
		return users.map(UserMapping::asDto);
	}
}
