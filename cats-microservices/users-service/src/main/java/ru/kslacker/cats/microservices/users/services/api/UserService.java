package ru.kslacker.cats.microservices.users.services.api;


import java.util.List;
import ru.kslacker.cats.microservices.users.dto.UserCreateDto;
import ru.kslacker.cats.microservices.users.dto.UserDetails;
import ru.kslacker.cats.microservices.users.dto.UserDto;
import ru.kslacker.cats.microservices.users.dto.UserSearchOptions;
import ru.kslacker.cats.microservices.users.dto.UserUpdateInformation;

public interface UserService {

	UserDto create(UserCreateDto createDto);

	UserDto get(Long id);

	List<UserDto> getBy(UserSearchOptions searchOptions);

	UserDetails loadByUsername(String username);

	boolean delete(Long id);

	UserDto disable(Long id);

	UserDto enable(Long id);

	UserDto ban(Long id);

	UserDto unban(Long id);

	UserDto update(UserUpdateInformation userUpdateInformation);

	UserDto promoteToAdmin(Long id);
}
