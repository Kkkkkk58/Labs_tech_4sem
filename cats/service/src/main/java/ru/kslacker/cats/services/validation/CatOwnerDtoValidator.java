package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.dataaccess.repositories.api.CatOwnerRepository;
import ru.kslacker.cats.services.dto.CatOwnerDto;

public class CatOwnerDtoValidator implements ConstraintValidator<ValidCatOwnerDto, CatOwnerDto> {

	@Autowired private CatOwnerRepository catOwnerService;

	@Override
	public boolean isValid(CatOwnerDto value, ConstraintValidatorContext context) {
		return value.id() != null && catOwnerService.existsById(value.id()) &&
			(value.name() == null || !value.name().isBlank());
	}
}
