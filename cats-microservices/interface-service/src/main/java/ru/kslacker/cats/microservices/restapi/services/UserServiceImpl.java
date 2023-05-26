package ru.kslacker.cats.microservices.restapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.stereotype.Service;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserCreateDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserUpdateInformation;
import ru.kslacker.cats.microservices.restapi.services.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.restapi.services.api.UserService;

@Service
public class UserServiceImpl extends AmqpRelyingService implements UserService {


	protected UserServiceImpl(
		AsyncAmqpTemplate asyncAmqpTemplate,
		Exchange exchange,
		ObjectMapper objectMapper) {

		super(asyncAmqpTemplate, exchange, objectMapper);
	}

	@Override
	public UserDto create(UserCreateDto createDto) {
		return handleRequest("user.create", createDto, UserDto.class);
	}

	@Override
	public UserDto get(Long id) {
		return handleRequest("user.get", id, UserDto.class);
	}

	@Override
	public List<UserDto> getBy(UserSearchOptions searchOptions) {
		return Arrays.stream(handleRequest("user.getby", searchOptions, UserDto[].class)).toList();
	}

	@Override
	public void delete(Long id) {
		handleRequest("user.delete", id, Boolean.class);
	}

	@Override
	public void disable(Long id) {
		handleRequest("user.enabled.false", id, UserDto.class);
	}

	@Override
	public void enable(Long id) {
		handleRequest("user.enabled.true", id, UserDto.class);
	}

	@Override
	public void ban(Long id) {
		handleRequest("user.ban.true", id, UserDto.class);
	}

	@Override
	public void unban(Long id) {
		handleRequest("user.ban.false", id, UserDto.class);
	}

	@Override
	public UserDto update(UserUpdateInformation userUpdateInformation) {
		return handleRequest("user.update", userUpdateInformation, UserDto.class);
	}

	@Override
	public void promoteToAdmin(Long id) {
		handleRequest("user.promote", id, UserDto.class);
	}
}
