package ru.kslacker.cats.services.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.User;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.UserDto;

@UtilityClass
public class UserMapping {

	public static UserDto asDto(User user) {
		CatOwnerDto catOwnerDto = getCatOwnerDto(user);

		return UserDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.email(user.getEmail())
			.owner(catOwnerDto)
			.enabled(user.isEnabled())
			.locked(user.isLocked())
			.accountExpirationDate(user.getAccountExpirationDate())
			.credentialsExpirationDate(user.getCredentialsExpirationDate())
			.build();
	}

	private static CatOwnerDto getCatOwnerDto(User user) {
		CatOwner owner = user.getOwner();
		return (owner == null) ? null : CatOwnerMapping.asDto(user.getOwner());
	}

}
