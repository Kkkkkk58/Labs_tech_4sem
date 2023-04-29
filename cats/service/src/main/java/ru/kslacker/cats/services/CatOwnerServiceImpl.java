package ru.kslacker.cats.services;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.dataaccess.specifications.CatOwnerSpecifications.withCat;
import static ru.kslacker.cats.dataaccess.specifications.CatOwnerSpecifications.withDateOfBirth;
import static ru.kslacker.cats.dataaccess.specifications.CatOwnerSpecifications.withName;

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
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.exceptions.EntityException;
import ru.kslacker.cats.services.mapping.CatOwnerMapping;
import ru.kslacker.cats.services.mapping.StreamMapping;
import ru.kslacker.cats.services.models.catowners.CatOwnerInformation;
import ru.kslacker.cats.services.models.catowners.CatOwnerSearchOptions;
import ru.kslacker.cats.services.models.catowners.CatOwnerUpdateInformation;
import ru.kslacker.cats.services.validation.service.api.ValidationService;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({CatOwnerMapping.class, StreamMapping.class})
public class CatOwnerServiceImpl implements CatOwnerService {

	private final ValidationService validator;
	private final CatOwnerRepository catOwnerRepository;
	private final CatRepository catRepository;

	@Autowired
	public CatOwnerServiceImpl(
		@NonNull ValidationService validator,
		@NonNull CatOwnerRepository catOwnerRepository,
		@NonNull CatRepository catRepository) {

		this.validator = validator;
		this.catOwnerRepository = catOwnerRepository;
		this.catRepository = catRepository;
	}

	@Override
	@Transactional
	public CatOwnerDto create(@NonNull CatOwnerInformation catOwnerInformation) {

		validator.validate(catOwnerInformation);
		CatOwner owner = new CatOwner(catOwnerInformation.name(),
			catOwnerInformation.dateOfBirth());
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
	public List<CatOwnerDto> getBy(@NonNull CatOwnerSearchOptions searchOptions) {

		Specification<CatOwner> specification =
			where(withName(searchOptions.name()))
				.and(withDateOfBirth(searchOptions.dateOfBirth()));

		for (Long id : Optional.ofNullable(searchOptions.catsIds())
			.orElse(Collections.emptyList())) {
			Cat cat = getCatById(id);
			specification = specification.and(withCat(cat));
		}

		return catOwnerRepository.findAll(specification, searchOptions.pageable()).stream()
			.asCatOwnerDto()
			.toList();
	}

	@Override
	public boolean exists(@NonNull Long id) {
		return catOwnerRepository.existsById(id);
	}

	@Override
	@Transactional
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

	private Cat getCatById(Long id) {
		return catRepository.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(Cat.class, id));
	}

}
