package ru.kslacker.cats.microservices.catowners.services;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.microservices.catowners.dataaccess.specifications.CatOwnerSpecifications.withCat;
import static ru.kslacker.cats.microservices.catowners.dataaccess.specifications.CatOwnerSpecifications.withDateOfBirth;
import static ru.kslacker.cats.microservices.catowners.dataaccess.specifications.CatOwnerSpecifications.withName;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.microservices.catowners.dataaccess.repositories.api.CatOwnerRepository;
import ru.kslacker.cats.microservices.catowners.dto.CatOwnerDto;
import ru.kslacker.cats.microservices.catowners.dto.CatOwnerInformation;
import ru.kslacker.cats.microservices.catowners.dto.CatOwnerSearchOptions;
import ru.kslacker.cats.microservices.catowners.dto.CatOwnerUpdateInformation;
import ru.kslacker.cats.microservices.catowners.mapping.CatOwnerMapping;
import ru.kslacker.cats.microservices.catowners.services.api.CatOwnerService;
import ru.kslacker.cats.microservices.catowners.validation.service.api.ValidationService;
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.jpaentities.entities.Cat;
import ru.kslacker.cats.microservices.jpaentities.entities.CatOwner;
import ru.kslacker.cats.microservices.jpaentities.exceptions.EntityException;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({CatOwnerMapping.class})
public class CatOwnerServiceImpl implements CatOwnerService {

	private final ValidationService validator;
	private final CatOwnerRepository catOwnerRepository;
	private final AmqpRelyingService amqpService;

	@Autowired
	public CatOwnerServiceImpl(
		@NonNull ValidationService validator,
		@NonNull CatOwnerRepository catOwnerRepository,
		@NonNull AmqpRelyingService amqpService) {

		this.validator = validator;
		this.catOwnerRepository = catOwnerRepository;
		this.amqpService = amqpService;
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{createOwnerQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public CatOwnerDto create(@NonNull CatOwnerInformation catOwnerInformation) {

		validator.validate(catOwnerInformation);
		CatOwner owner = new CatOwner(catOwnerInformation.name(),
			catOwnerInformation.dateOfBirth());
		return catOwnerRepository.saveAndFlush(owner).asDto();
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{deleteOwnerQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public boolean delete(@NonNull Long id) {

		CatOwner owner = getCatOwnerById(id);

		for (Cat cat : owner.getCats().stream().toList()) {
			try {
				amqpService.handleRequest("cat.delete", cat.getId(), Boolean.class);
			} catch (EntityException ignored) {

			}
		}

		catOwnerRepository.delete(owner);
		return true;
	}

	@Override
	@RabbitListener(queues = "#{getOwnerQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public CatOwnerDto get(@NonNull Long id) {
		return getCatOwnerById(id).asDto();
	}

	@Override
	@RabbitListener(queues = "#{getOwnerByParamsQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public List<CatOwnerDto> getBy(@NonNull CatOwnerSearchOptions searchOptions) {

		Specification<CatOwner> specification =
			where(withName(searchOptions.name()))
				.and(withDateOfBirth(searchOptions.dateOfBirth()));

		for (Long catId : Optional.ofNullable(searchOptions.catsIds())
			.orElse(Collections.emptyList())) {

			specification = specification.and(withCat(catId));
		}

		return catOwnerRepository.findAll(specification, searchOptions.pageable()).stream()
			.asCatOwnerDto()
			.toList();
	}

	@Override
	@RabbitListener(queues = "#{ownerExistsQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public boolean exists(@NonNull Long id) {
		return catOwnerRepository.existsById(id);
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{updateOwnerQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public CatOwnerDto update(@NonNull CatOwnerUpdateInformation catOwnerUpdateInformation) {

		validator.validate(catOwnerUpdateInformation);

		CatOwner owner = getCatOwnerById(catOwnerUpdateInformation.id());

		if (catOwnerUpdateInformation.name() != null) {
			owner.setName(catOwnerUpdateInformation.name());
		}
		if (catOwnerUpdateInformation.dateOfBirth() != null) {
			owner.setDateOfBirth(catOwnerUpdateInformation.dateOfBirth());
		}

		return catOwnerRepository.save(owner).asDto();
	}

	private CatOwner getCatOwnerById(Long id) {
		return catOwnerRepository.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(CatOwner.class, id));
	}

}
