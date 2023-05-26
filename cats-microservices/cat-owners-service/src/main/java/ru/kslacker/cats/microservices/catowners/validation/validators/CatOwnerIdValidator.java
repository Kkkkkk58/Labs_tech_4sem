package ru.kslacker.cats.microservices.catowners.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.microservices.catowners.dataaccess.repositories.api.CatOwnerRepository;
import ru.kslacker.cats.microservices.catowners.validation.annotations.ValidCatOwnerId;

public class CatOwnerIdValidator implements ConstraintValidator<ValidCatOwnerId, Long> {

	@Autowired
	private CatOwnerRepository catOwnerRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return catOwnerRepository.existsById(value);
	}
}
