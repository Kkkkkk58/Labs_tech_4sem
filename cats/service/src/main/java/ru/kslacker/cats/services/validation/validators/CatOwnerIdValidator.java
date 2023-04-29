package ru.kslacker.cats.services.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.services.validation.annotations.ValidCatOwnerId;

public class CatOwnerIdValidator implements ConstraintValidator<ValidCatOwnerId, Long> {

	@Autowired
	private CatOwnerRepository catOwnerRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return catOwnerRepository.existsById(value);
	}
}
