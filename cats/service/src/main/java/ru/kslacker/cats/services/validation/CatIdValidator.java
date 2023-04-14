package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.dataaccess.repositories.CatRepository;

public class CatIdValidator implements ConstraintValidator<ValidCatId, Long> {

	@Autowired private CatRepository catRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return catRepository.existsById(value);
	}
}
