package ru.kslacker.cats.services;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.dataaccess.specifications.CatSpecifications.withBreed;
import static ru.kslacker.cats.dataaccess.specifications.CatSpecifications.withDateOfBirth;
import static ru.kslacker.cats.dataaccess.specifications.CatSpecifications.withFriend;
import static ru.kslacker.cats.dataaccess.specifications.CatSpecifications.withFurColor;
import static ru.kslacker.cats.dataaccess.specifications.CatSpecifications.withName;
import static ru.kslacker.cats.dataaccess.specifications.CatSpecifications.withOwnerId;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.CatRepository;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.exceptions.EntityException;
import ru.kslacker.cats.services.mapping.CatMapping;
import ru.kslacker.cats.services.mapping.StreamMapping;
import ru.kslacker.cats.services.models.cats.CatInformation;
import ru.kslacker.cats.services.models.cats.CatSearchOptions;
import ru.kslacker.cats.services.models.cats.CatUpdateInformation;
import ru.kslacker.cats.services.validation.service.api.ValidationService;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({CatMapping.class, StreamMapping.class})
public class CatServiceImpl implements CatService {

	private final ValidationService validator;
	private final CatRepository catRepository;
	private final CatOwnerRepository catOwnerRepository;

	@Autowired
	public CatServiceImpl(
		@NonNull ValidationService validator,
		@NonNull CatRepository catRepository,
		@NonNull CatOwnerRepository catOwnerRepository) {

		this.validator = validator;
		this.catRepository = catRepository;
		this.catOwnerRepository = catOwnerRepository;
	}

	@Override
	@Transactional
	public CatDto create(@NonNull CatInformation catInformation) {

		validator.validate(catInformation);
		CatOwner owner = catOwnerRepository.getReferenceById(catInformation.catOwnerId());
		Cat cat = new Cat(
			catInformation.name(),
			catInformation.dateOfBirth(),
			catInformation.breed(),
			catInformation.furColor(),
			owner);

		return catRepository.saveAndFlush(cat).asDto();
	}

	@Override
	@Transactional
	public void delete(@NonNull Long id) {

		Cat cat = getCatById(id);

		List<Cat> friends = cat.getFriends().stream().toList();
		for (Cat friend : friends) {
			friend.removeFriend(cat);
		}
		if (cat.getOwner() != null) {
			cat.getOwner().removeCat(cat);
		}

		catRepository.delete(cat);
	}

	@Override
	public CatDto get(@NonNull Long id) {
		return getCatById(id).asDto();
	}

	@Override
	public List<CatDto> getBy(@NonNull CatSearchOptions searchOptions) {

		Specification<Cat> specification =
			where(withName(searchOptions.name()))
				.and(withDateOfBirth(searchOptions.dateOfBirth()))
				.and(withBreed(searchOptions.breed()))
				.and(withFurColor(searchOptions.furColor()))
				.and(withOwnerId(searchOptions.ownerId()));

		for (Long id : Optional.ofNullable(searchOptions.friendsIds())
			.orElse(Collections.emptyList())) {
			Cat friend = getCatById(id);
			specification = specification.and(withFriend(friend));
		}

		return catRepository.findAll(specification, searchOptions.pageable()).stream().asCatDto()
			.toList();
	}

	@Override
	@Transactional
	public void makeFriends(@NonNull Long cat1Id, @NonNull Long cat2Id) {

		Cat cat1 = getCatById(cat1Id);
		Cat cat2 = getCatById(cat2Id);

		cat1.addFriend(cat2);
		catRepository.save(cat1);
	}

	@Override
	@Transactional
	public void removeFriend(@NonNull Long cat1Id, @NonNull Long cat2Id) {

		Cat cat1 = getCatById(cat1Id);
		Cat cat2 = getCatById(cat2Id);

		cat1.removeFriend(cat2);
		catRepository.save(cat1);
	}

	@Override
	public boolean exists(@NonNull Long id) {
		return catRepository.existsById(id);
	}

	@Override
	@Transactional
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

	private Cat getCatById(Long id) {
		return catRepository.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(Cat.class, id));
	}
}
