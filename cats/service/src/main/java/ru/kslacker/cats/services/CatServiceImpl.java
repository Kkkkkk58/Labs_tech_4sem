package ru.kslacker.cats.services;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.dataaccess.specifications.CatFieldSpecifications.withBreed;
import static ru.kslacker.cats.dataaccess.specifications.CatFieldSpecifications.withDateOfBirth;
import static ru.kslacker.cats.dataaccess.specifications.CatFieldSpecifications.withFriend;
import static ru.kslacker.cats.dataaccess.specifications.CatFieldSpecifications.withFurColor;
import static ru.kslacker.cats.dataaccess.specifications.CatFieldSpecifications.withName;
import static ru.kslacker.cats.dataaccess.specifications.CatFieldSpecifications.withOwnerId;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.CatRepository;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatUpdateDto;
import ru.kslacker.cats.services.exceptions.EntityException;
import ru.kslacker.cats.services.mapping.CatMapping;
import ru.kslacker.cats.services.mapping.StreamMapping;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({CatMapping.class, StreamMapping.class})
public class CatServiceImpl implements CatService {

	private final Validator validator;
	private final CatRepository catRepository;
	private final CatOwnerRepository catOwnerRepository;

	@Autowired
	public CatServiceImpl(
		@NonNull Validator validator,
		@NonNull CatRepository catRepository,
		@NonNull CatOwnerRepository catOwnerRepository) {

		this.validator = validator;
		this.catRepository = catRepository;
		this.catOwnerRepository = catOwnerRepository;
	}

	@Override
	@Transactional
	public CatDto create(
		@NonNull String name,
		@NonNull LocalDate dateOfBirth,
		@NonNull String breed,
		@NonNull FurColor furColor,
		@NonNull Long catOwnerId) {

		CatOwner owner = catOwnerRepository.getReferenceById(catOwnerId);
		Cat cat = new Cat(name, dateOfBirth, breed, furColor, owner);

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
	public List<CatDto> getBy(String name, LocalDate dateOfBirth, String breed, FurColor furColor,
		Long ownerId, List<Long> friendsIds, Pageable pageable) {

		Specification<Cat> specification = where(
			withName(name)).and(withDateOfBirth(dateOfBirth)).and(withBreed(breed))
			.and(withFurColor(furColor)).and(withOwnerId(ownerId));

		for (Long id : Optional.ofNullable(friendsIds).orElse(Collections.emptyList())) {
			Cat friend = getCatById(id);
			specification = specification.and(withFriend(friend));
		}

		return catRepository.findAll(specification, pageable).stream().asCatDto().toList();
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
	public boolean exists(Long id) {
		return catRepository.existsById(id);
	}

	@Override
	@Transactional
	public CatDto update(CatUpdateDto catDto) {

		validateUpdateDto(catDto);

		Cat cat = getCatById(catDto.id());

		if (catDto.name() != null) {
			cat.setName(catDto.name());
		}
		if (catDto.dateOfBirth() != null) {
			cat.setDateOfBirth(catDto.dateOfBirth());
		}
		if (catDto.breed() != null) {
			cat.setBreed(catDto.breed());
		}
		if (catDto.furColor() != null) {
			cat.setFurColor(catDto.furColor());
		}

		return catRepository.save(cat).asDto();
	}

	private void validateUpdateDto(CatUpdateDto catDto) {
		Set<ConstraintViolation<CatUpdateDto>> violations = validator.validate(catDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	private Cat getCatById(Long id) {
		return catRepository.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(Cat.class, id));
	}
}
