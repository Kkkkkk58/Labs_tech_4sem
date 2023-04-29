package ru.kslacker.cats.services.api;

import java.util.List;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.models.catowners.CatOwnerInformation;
import ru.kslacker.cats.services.models.users.Credentials;
import ru.kslacker.cats.services.models.users.UserSearchOptions;
import ru.kslacker.cats.services.models.users.UserUpdateInformation;

public interface UserService {

	UserDto create(
		Credentials credentials,
		UserRole role,
		CatOwnerInformation catOwnerInformation);

	UserDto create(
		Credentials credentials,
		CatOwnerInformation catOwnerInformation);

	UserDto get(Long id);

	List<UserDto> getBy(UserSearchOptions searchOptions);

	void delete(Long id);

	void disable(Long id);

	void enable(Long id);

	void ban(Long id);

	void unban(Long id);

	UserDto update(UserUpdateInformation userUpdateInformation);

	void promoteToAdmin(Long id);
}
