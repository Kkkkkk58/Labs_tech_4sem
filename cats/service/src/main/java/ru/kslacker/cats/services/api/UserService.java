package ru.kslacker.cats.services.api;

import ru.kslacker.cats.services.dto.UserDto;

public interface UserService {

	UserDto create(String username, String email, String password, Long ownerId);
	UserDto get(Long id);
	void delete(Long id);
}
