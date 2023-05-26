package ru.kslacker.cats.microservices.restapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerUpdateInformation;
import ru.kslacker.cats.microservices.restapi.services.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.restapi.services.api.CatOwnerService;

@Service
public class CatOwnerServiceImpl extends AmqpRelyingService implements CatOwnerService  {

	@Autowired
	public CatOwnerServiceImpl(AsyncAmqpTemplate amqpTemplate, Exchange exchange, ObjectMapper objectMapper) {
		super(amqpTemplate, exchange, objectMapper);
	}

	@Override
	public CatOwnerDto create(CatOwnerInformation catOwnerInformation) {
		return handleRequest("owner.create", catOwnerInformation, CatOwnerDto.class);
	}

	@Override
	public void delete(Long id) {
		handleRequest("owner.delete", id, Boolean.class);
	}

	@Override
	public CatOwnerDto get(Long id) {
		return handleRequest("owner.get", id, CatOwnerDto.class);
	}

	@Override
	public List<CatOwnerDto> getBy(CatOwnerSearchOptions searchOptions) {

		return Arrays.stream(handleRequest("owner.getby", searchOptions, CatOwnerDto[].class)).toList();
	}

	@Override
	public CatOwnerDto update(CatOwnerUpdateInformation catOwnerUpdateInformation) {
		return handleRequest("owner.update", catOwnerUpdateInformation, CatOwnerDto.class);
	}
}
