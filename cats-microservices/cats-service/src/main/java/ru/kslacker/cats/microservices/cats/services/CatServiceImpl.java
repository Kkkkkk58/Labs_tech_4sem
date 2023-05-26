package ru.kslacker.cats.microservices.cats.services;

import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.amqp.AmqpTimeoutException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.microservices.cats.dataaccess.repositories.api.CatRepository;
import ru.kslacker.cats.microservices.cats.dto.CatDto;
import ru.kslacker.cats.microservices.cats.dto.CatInformation;
import ru.kslacker.cats.microservices.cats.dto.CatSearchOptions;
import ru.kslacker.cats.microservices.cats.dto.CatUpdateInformation;
import ru.kslacker.cats.microservices.cats.dto.FriendsInfo;
import ru.kslacker.cats.microservices.cats.mapping.CatMapping;
import ru.kslacker.cats.microservices.cats.services.api.CatService;
import ru.kslacker.cats.microservices.cats.validation.service.api.ValidationService;
import ru.kslacker.cats.microservices.jpaentities.entities.Cat;
import ru.kslacker.cats.microservices.jpaentities.exceptions.EntityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.microservices.cats.dataaccess.specifications.CatSpecifications.withBreed;
import static ru.kslacker.cats.microservices.cats.dataaccess.specifications.CatSpecifications.withDateOfBirth;
import static ru.kslacker.cats.microservices.cats.dataaccess.specifications.CatSpecifications.withFriend;
import static ru.kslacker.cats.microservices.cats.dataaccess.specifications.CatSpecifications.withFurColor;
import static ru.kslacker.cats.microservices.cats.dataaccess.specifications.CatSpecifications.withName;
import static ru.kslacker.cats.microservices.cats.dataaccess.specifications.CatSpecifications.withOwnerId;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({CatMapping.class})
public class CatServiceImpl implements CatService {

	private final ValidationService validator;
	private final CatRepository catRepository;
	private final Exchange exchange;
	private final AmqpTemplate amqpTemplate;


	@Autowired
	public CatServiceImpl(
		@NonNull ValidationService validator,
		@NonNull CatRepository catRepository,
		@NonNull Exchange exchange,
		@NonNull AmqpTemplate amqpTemplate) {

		this.validator = validator;
		this.catRepository = catRepository;
		this.exchange = exchange;
		this.amqpTemplate = amqpTemplate;
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{createCatQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public CatDto create(@NonNull CatInformation catInformation) {

		validator.validate(catInformation);

		validateOwnerPresence(catInformation.catOwnerId());

		Cat cat = new Cat(
			catInformation.name(),
			catInformation.dateOfBirth(),
			catInformation.breed(),
			catInformation.furColor(),
			catInformation.catOwnerId());

		return catRepository.saveAndFlush(cat).asDto();
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{deleteCatQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public boolean delete(@NonNull Long id) {

		Cat cat = getCatById(id);

		List<Cat> friends = cat.getFriends().stream().toList();
		for (Cat friend : friends) {
			friend.removeFriend(cat);
		}
		// TODO owner delete cat

		catRepository.delete(cat);
		return true;
	}

	@Override
	@RabbitListener(queues = "#{getCatQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public CatDto get(@NonNull Long id) {
		return getCatById(id).asDto();
	}

	@Override
	@RabbitListener(queues = "#{getCatByParamsQueue.name}", id = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public List<CatDto> getBy(@NonNull CatSearchOptions searchOptions) {

		Specification<Cat> specification =
			where(withName(searchOptions.name()))
				.and(withDateOfBirth(searchOptions.dateOfBirth()))
				.and(withBreed(searchOptions.breed()))
				.and(withFurColor(searchOptions.furColor()))
				.and(withOwnerId(searchOptions.ownerId()));

		for (Long id : Optional.ofNullable(searchOptions.friendsIds()).orElse(Collections.emptyList())) {
			Cat friend = getCatById(id);
			specification = specification.and(withFriend(friend));
		}

		return catRepository.findAll(specification, searchOptions.pageable()).stream().asCatDto()
			.toList();
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{addFriendQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public boolean makeFriends(@NonNull FriendsInfo friendsInfo) {

		Cat cat1 = getCatById(friendsInfo.cat1Id());
		Cat cat2 = getCatById(friendsInfo.cat2Id());

		cat1.addFriend(cat2);
		catRepository.save(cat1);
		return true;
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{deleteFriendQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public boolean removeFriend(@NonNull FriendsInfo friendsInfo) {

		Cat cat1 = getCatById(friendsInfo.cat1Id());
		Cat cat2 = getCatById(friendsInfo.cat2Id());

		cat1.removeFriend(cat2);
		catRepository.save(cat1);
		return true;
	}

	@Override
	@RabbitListener(queues = "#{catExistsQueue.name}", group = "#{amqpGroupName}",
		returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public boolean exists(@NonNull Long id) {
		return catRepository.existsById(id);
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{updateCatQueue.name}", group = "#{amqpGroupName}")
	public CatDto update(@NonNull CatUpdateInformation catUpdateInformation) {

		validator.validate(catUpdateInformation);

		Cat cat = getCatById(catUpdateInformation.id());

		if (catUpdateInformation.name() != null) {
			cat.setName(catUpdateInformation.name());
		}
		if (catUpdateInformation.dateOfBirth() != null) {
			cat.setDateOfBirth(catUpdateInformation.dateOfBirth());
		}
		if (catUpdateInformation.breed() != null) {
			cat.setBreed(catUpdateInformation.breed());
		}
		if (catUpdateInformation.furColor() != null) {
			cat.setFurColor(catUpdateInformation.furColor());
		}

		return catRepository.save(cat).asDto();
	}

	// TODO add NotFound exception
	// TODO Use async request handler
	private Cat getCatById(Long id) {
		return catRepository.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(Cat.class, id));
	}

	private void validateOwnerPresence(Long catOwnerId) {
		Boolean ownerExists = isOwnerExist(catOwnerId);

		if (ownerExists == null) {
			throw new AmqpTimeoutException("//TODO");
		}

		// TODO
		if (!Boolean.TRUE.equals(ownerExists)) {
			throw new RuntimeException("//TODO");
		}
	}

	private Boolean isOwnerExist(Long catOwnerId) {
		return amqpTemplate.convertSendAndReceiveAsType(
			exchange.getName(),
			"owner.exists",
			catOwnerId,
			new ParameterizedTypeReference<>() {
			});
	}
}

