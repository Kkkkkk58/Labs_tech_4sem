package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.dataaccess.repositories.api.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.api.CatRepository;
import ru.kslacker.cats.services.dto.CatDto;

public class CatDtoValidator implements ConstraintValidator<ValidCatDto, CatDto> {

	@Autowired private CatRepository catService;
	@Autowired private CatOwnerRepository catOwnerService;

	@Override
	public boolean isValid(CatDto value, ConstraintValidatorContext context) {
		return value.id() != null && catService.existsById(value.id()) &&
			(value.name() == null || !value.name().isBlank()) &&
			(value.ownerId() == null || catOwnerService.existsById(value.id())) &&
			(value.friends() == null || (
				!value.friends().contains(value.id()) &&
					value.friends().size() == value.friends().stream().distinct().count() &&
					value.friends().stream().allMatch(id -> catService.existsById(id))
			));
	}
}
