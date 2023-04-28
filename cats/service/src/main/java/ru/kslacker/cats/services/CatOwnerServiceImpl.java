package ru.kslacker.cats.services;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.dataaccess.specifications.CatOwnerSpecifications.withCat;
import static ru.kslacker.cats.dataaccess.specifications.CatOwnerSpecifications.withDateOfBirth;
import static ru.kslacker.cats.dataaccess.specifications.CatOwnerSpecifications.withName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
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
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.CatRepository;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.models.CatOwnerUpdateModel;
import ru.kslacker.cats.services.exceptions.EntityException;
import ru.kslacker.cats.services.mapping.CatOwnerMapping;
import ru.kslacker.cats.services.mapping.StreamMapping;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({CatOwnerMapping.class, StreamMapping.class})
public class CatOwnerServiceImpl implements CatOwnerService {

	private final Validator validator;
	private final CatOwnerRepository catOwnerRepository;
	private final CatRepository catRepository;

	@Autowired
	public CatOwnerServiceImpl(
		@NonNull Validator validator,
		@NonNull CatOwnerRepository catOwnerRepository,
		@NonNull CatRepository catRepository) {

		this.validator = validator;
		this.catOwnerRepository = catOwnerRepository;
		this.catRepository = catRepository;
	}

	@Override
	@Transactional
	public CatOwnerDto create(@NotBlank String name, @NonNull LocalDate dateOfBirth) {
		CatOwner owner = new CatOwner(name, dateOfBirth);
		return catOwnerRepository.saveAndFlush(owner).asDto();
	}

	@Override
	@Transactional
	public void delete(@NonNull Long id) {
		CatOwner owner = getCatOwnerById(id);
		catOwnerRepository.delete(owner);
	}

	@Override
	public CatOwnerDto get(@NonNull Long id) {
		return getCatOwnerById(id).asDto();
	}

	@Override
	public List<CatOwnerDto> getBy(String name, LocalDate dateOfBirth, List<Long> catsIds,
		Pageable pageable) {

		Specification<CatOwner> specification = where(withName(name)).and(
			withDateOfBirth(dateOfBirth));
		for (Long id : Optional.ofNullable(catsIds).orElse(Collections.emptyList())) {
			Cat cat = getCatById(id);
			specification = specification.and(withCat(cat));
		}

		return catOwnerRepository.findAll(specification, pageable).stream().asCatOwnerDto()
			.toList();
	}

	@Override
	public boolean exists(Long id) {
		return catOwnerRepository.existsById(id);
	}

	@Override
	@Transactional
	public CatOwnerDto update(CatOwnerUpdateModel catOwnerDto) {

		validateUpdateModel(catOwnerDto);

		CatOwner owner = getCatOwnerById(catOwnerDto.id());

		if (catOwnerDto.name() != null) {
			owner.setName(catOwnerDto.name());
		}
		if (catOwnerDto.dateOfBirth() != null) {
			owner.setDateOfBirth(catOwnerDto.dateOfBirth());
		}

		return catOwnerRepository.save(owner).asDto();
	}

	private void validateUpdateModel(CatOwnerUpdateModel updateModel) {
		Set<ConstraintViolation<CatOwnerUpdateModel>> violations = validator.validate(
			updateModel);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	private CatOwner getCatOwnerById(Long id) {
		return catOwnerRepository.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(CatOwner.class, id));
	}

	private Cat getCatById(Long id) {
		return catRepository.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(Cat.class, id));
	}

}
