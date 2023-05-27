package ru.kslacker.cats.microservices.restapi.services;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatUpdateInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.FriendsInfo;
import ru.kslacker.cats.microservices.restapi.services.api.CatService;

@Service
public class CatServiceImpl implements CatService {

	private final AmqpRelyingService amqpService;

	@Autowired
	public CatServiceImpl(AmqpRelyingService amqpService) {
		this.amqpService = amqpService;
	}

	@Override
	public CatDto create(CatInformation catInformation) {
		return amqpService.handleRequest("cat.create", catInformation, CatDto.class);
	}

	@Override
	public void delete(Long id) {
		amqpService.handleRequest("cat.delete", id, Boolean.class);
	}

	@Override
	public CatDto get(Long id) {
		return amqpService.handleRequest("cat.get", id, CatDto.class);
	}

	@Override
	public List<CatDto> getBy(CatSearchOptions searchOptions) {
		return Arrays.stream(amqpService.handleRequest("cat.getby", searchOptions, CatDto[].class)).toList();
	}

	@Override
	public void makeFriends(FriendsInfo friendsInfo) {
		amqpService.handleRequest("cat.friend.add", friendsInfo, Boolean.class);
	}

	@Override
	public void removeFriend(FriendsInfo friendsInfo) {
		amqpService.handleRequest("cat.friend.delete", friendsInfo, Boolean.class);
	}

	@Override
	public boolean exists(Long id) {
		return amqpService.handleRequest("cat.exists", id, Boolean.class);
	}

	@Override
	public CatDto update(CatUpdateInformation catUpdateInformation) {
		return amqpService.handleRequest("cat.update", catUpdateInformation, CatDto.class);
	}
}
