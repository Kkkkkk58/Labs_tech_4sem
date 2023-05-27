package ru.kslacker.cats.microservices.restapi.services.api;


import java.util.List;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserCreateDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserUpdateInformation;

public interface UserService {

	UserDto create(UserCreateDto createDto);

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
