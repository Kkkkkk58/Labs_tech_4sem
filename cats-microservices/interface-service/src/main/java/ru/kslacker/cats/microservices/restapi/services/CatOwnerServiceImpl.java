package ru.kslacker.cats.microservices.restapi.services;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerUpdateInformation;
import ru.kslacker.cats.microservices.restapi.services.api.CatOwnerService;

@Service
public class CatOwnerServiceImpl implements CatOwnerService  {

	private final AmqpRelyingService amqpService;

	@Autowired
	public CatOwnerServiceImpl(AmqpRelyingService amqpService) {
		this.amqpService = amqpService;
	}

	@Override
	public CatOwnerDto create(CatOwnerInformation catOwnerInformation) {
		return amqpService.handleRequest("owner.create", catOwnerInformation, CatOwnerDto.class);
	}

	@Override
	public void delete(Long id) {
		amqpService.handleRequest("owner.delete", id, Boolean.class);
	}

	@Override
	public CatOwnerDto get(Long id) {
		return amqpService.handleRequest("owner.get", id, CatOwnerDto.class);
	}

	@Override
	public List<CatOwnerDto> getBy(CatOwnerSearchOptions searchOptions) {

		return Arrays.stream(amqpService.handleRequest("owner.getby", searchOptions, CatOwnerDto[].class)).toList();
	}

	@Override
	public CatOwnerDto update(CatOwnerUpdateInformation catOwnerUpdateInformation) {
		return amqpService.handleRequest("owner.update", catOwnerUpdateInformation, CatOwnerDto.class);
	}
}
