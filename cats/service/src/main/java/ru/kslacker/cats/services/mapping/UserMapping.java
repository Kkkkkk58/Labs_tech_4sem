package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.UserAccount;
import ru.kslacker.cats.services.dto.UserDto;

@UtilityClass
public class UserMapping {

	public static UserDto asDto(UserAccount userAccount) {
		return new UserDto(userAccount.getId(), userAccount.getUsername());
	}

}
