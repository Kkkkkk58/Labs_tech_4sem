package ru.kslacker.cats.microservices.restapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.stereotype.Service;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatUpdateInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.FriendsInfo;
import ru.kslacker.cats.microservices.restapi.services.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.restapi.services.api.CatService;
import java.util.Arrays;
import java.util.List;

@Service
public class CatServiceImpl extends AmqpRelyingService implements CatService {

	protected CatServiceImpl(AsyncAmqpTemplate amqpTemplate, Exchange exchange, ObjectMapper objectMapper) {
		super(amqpTemplate, exchange, objectMapper);
	}

	@Override
	public CatDto create(CatInformation catInformation) {
		return handleRequest("cat.create", catInformation, CatDto.class);
	}

	@Override
	public void delete(Long id) {
//		TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
//		taskExecutor.execute(() -> new RabbitTemplate().convertAndSend("cat.delete", id));
		handleRequest("cat.delete", id, Boolean.class);
	}

	@Override
	public CatDto get(Long id) {
		return handleRequest("cat.get", id, CatDto.class);
	}

	@Override
	public List<CatDto> getBy(CatSearchOptions searchOptions) {
		return Arrays.stream(handleRequest("cat.getby", searchOptions, CatDto[].class)).toList();
	}

	@Override
	public void makeFriends(FriendsInfo friendsInfo) {
		handleRequest("cat.friend.add", friendsInfo, Boolean.class);
	}

	@Override
	public void removeFriend(FriendsInfo friendsInfo) {
		handleRequest("cat.friend.delete", friendsInfo, Boolean.class);
	}

	@Override
	public boolean exists(Long id) {
		return handleRequest("cat.exists", id, Boolean.class);
	}

	@Override
	public CatDto update(CatUpdateInformation catUpdateInformation) {
		return handleRequest("cat.update", catUpdateInformation, CatDto.class);
	}
}
