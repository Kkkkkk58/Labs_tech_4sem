package ru.kslacker.cats.microservices.cats.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.microservices.cats.dataaccess.repositories.api.CatRepository;
import ru.kslacker.cats.microservices.cats.validation.annotations.ValidCatId;

public class CatIdValidator implements ConstraintValidator<ValidCatId, Long> {

	@Autowired
	private CatRepository catRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return catRepository.existsById(value);
	}
}
