package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.User;
import ru.kslacker.cats.services.dto.UserDto;

@UtilityClass
public class UserMapping {

	public static UserDto asDto(User user) {
		return new UserDto(user.getId(), user.getUsername());
	}

}
