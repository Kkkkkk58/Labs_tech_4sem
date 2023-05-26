package ru.kslacker.cats.microservices.users.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.cats.microservices.jpaentities.entities.CatOwner;
import ru.kslacker.cats.microservices.jpaentities.entities.User;
import ru.kslacker.cats.microservices.users.dto.UserDto;
import java.util.stream.Stream;

@UtilityClass
public class UserMapping {

	public static UserDto asDto(User user) {
		return UserDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.email(user.getEmail())
			.ownerId(getCatOwnerId(user))
			.enabled(user.isEnabled())
			.locked(user.isLocked())
			.accountExpirationDate(user.getAccountExpirationDate())
			.credentialsExpirationDate(user.getCredentialsExpirationDate())
			.build();
	}

	public static Stream<UserDto> asUserDto(Stream<User> users) {
		return users.map(UserMapping::asDto);
	}

	private static Long getCatOwnerId(User user) {
		CatOwner owner = user.getOwner();
		return (owner == null) ? null : owner.getId();
	}

}
