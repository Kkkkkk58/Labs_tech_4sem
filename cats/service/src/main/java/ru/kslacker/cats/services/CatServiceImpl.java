package ru.kslacker.cats.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.repositories.api.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.api.CatRepository;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
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

		Cat cat = catRepository.getEntityById(id);

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
		return catRepository.getEntityById(id).asDto();
	}

	@Override
	public List<CatDto> getBy(@NonNull Predicate<Cat> condition) {
		return catRepository.findAll().stream().filter(condition).asCatDto().toList();
	}

	@Override
	public List<CatDto> getBy(@NonNull Map<String, Object> paramSet) {
		return catRepository.getBy(paramSet).stream().asCatDto().toList();
	}

	@Override
	@Transactional
	public void makeFriends(@NonNull Long cat1Id, @NonNull Long cat2Id) {

		Cat cat1 = catRepository.getEntityById(cat1Id);
		Cat cat2 = catRepository.getEntityById(cat2Id);

		cat1.addFriend(cat2);
		catRepository.save(cat1);
	}

	@Override
	@Transactional
	public void removeFriend(@NonNull Long cat1Id, @NonNull Long cat2Id) {

		Cat cat1 = catRepository.getEntityById(cat1Id);
		Cat cat2 = catRepository.getEntityById(cat2Id);

		cat1.removeFriend(cat2);
		catRepository.save(cat1);
	}

	@Override
	public boolean exists(Long id) {
		return catRepository.existsById(id);
	}

	@Override
	public CatDto update(CatDto catDto) {

		Set<ConstraintViolation<CatDto>> violations = validator.validate(catDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		Cat cat = catRepository.getEntityById(catDto.id());

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
		if (catDto.friends() != null) {
			cat.setFriends(new ArrayList<>(catDto.friends().stream().map(catRepository::getEntityById).toList()));
		}
		if (catDto.ownerId() != null) {
			cat.setOwner(catOwnerRepository.getEntityById(catDto.ownerId()));
		}

		return catRepository.save(cat).asDto();
	}
}
