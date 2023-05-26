package ru.kslacker.cats.microservices.restapi.services;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserCreateDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserUpdateInformation;
import ru.kslacker.cats.microservices.restapi.services.api.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final AmqpRelyingService amqpService;

	@Autowired
	public UserServiceImpl(AmqpRelyingService amqpService) {
		this.amqpService = amqpService;
	}

	@Override
	public UserDto create(UserCreateDto createDto) {
		return amqpService.handleRequest("user.create", createDto, UserDto.class);
	}

	@Override
	public UserDto get(Long id) {
		return amqpService.handleRequest("user.get", id, UserDto.class);
	}

	@Override
	public List<UserDto> getBy(UserSearchOptions searchOptions) {
		return Arrays.stream(amqpService.handleRequest("user.getby", searchOptions, UserDto[].class)).toList();
	}

	@Override
	public void delete(Long id) {
		amqpService.handleRequest("user.delete", id, Boolean.class);
	}

	@Override
	public void disable(Long id) {
		amqpService.handleRequest("user.enabled.false", id, UserDto.class);
	}

	@Override
	public void enable(Long id) {
		amqpService.handleRequest("user.enabled.true", id, UserDto.class);
	}

	@Override
	public void ban(Long id) {
		amqpService.handleRequest("user.ban.true", id, UserDto.class);
	}

	@Override
	public void unban(Long id) {
		amqpService.handleRequest("user.ban.false", id, UserDto.class);
	}

	@Override
	public UserDto update(UserUpdateInformation userUpdateInformation) {
		return amqpService.handleRequest("user.update", userUpdateInformation, UserDto.class);
	}

	@Override
	public void promoteToAdmin(Long id) {
		amqpService.handleRequest("user.promote", id, UserDto.class);
	}
}
