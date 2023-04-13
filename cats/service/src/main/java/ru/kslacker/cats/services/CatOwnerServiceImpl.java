package ru.kslacker.cats.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.repositories.api.CatOwnerRepository;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.CatOwnerUpdateDto;
import ru.kslacker.cats.services.mapping.CatOwnerMapping;
import ru.kslacker.cats.services.mapping.StreamMapping;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({CatOwnerMapping.class, StreamMapping.class})
public class CatOwnerServiceImpl implements CatOwnerService {

	private final Validator validator;
	private final CatOwnerRepository catOwnerRepository;

	@Autowired
	public CatOwnerServiceImpl(
		Validator validator,
		@NonNull CatOwnerRepository catOwnerRepository) {

		this.validator = validator;
		this.catOwnerRepository = catOwnerRepository;
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
		CatOwner owner = catOwnerRepository.getEntityById(id);
		catOwnerRepository.delete(owner);
	}

	@Override
	public CatOwnerDto get(@NonNull Long id) {
		return catOwnerRepository.getEntityById(id).asDto();
	}

	@Override
	public List<CatOwnerDto> getBy(@NonNull Predicate<CatOwner> condition) {
		return catOwnerRepository.findAll().stream().filter(condition).asCatOwnerDto().toList();
	}

	@Override
	public List<CatOwnerDto> getBy(@NonNull Map<String, Object> paramSet) {
		return catOwnerRepository.getBy(paramSet).stream().asCatOwnerDto().toList();
	}

	@Override
	public boolean exists(Long id) {
		return catOwnerRepository.existsById(id);
	}

	@Override
	public CatOwnerDto update(CatOwnerUpdateDto catOwnerDto) {

		Set<ConstraintViolation<CatOwnerUpdateDto>> violations = validator.validate(catOwnerDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		CatOwner owner = catOwnerRepository.getEntityById(catOwnerDto.id());

		if (catOwnerDto.name() != null) {
			owner.setName(catOwnerDto.name());
		}
		if (catOwnerDto.dateOfBirth() != null) {
			owner.setDateOfBirth(catOwnerDto.dateOfBirth());
		}

		return catOwnerRepository.save(owner).asDto();
	}


}
