package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.kslacker.cats.dataaccess.entities.CatOwner;

@Component
public class CatOwnerValidator implements ConstraintValidator<ValidCatOwner, CatOwner> {

	@Override
	public void initialize(ValidCatOwner constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(CatOwner value, ConstraintValidatorContext context) {
		return !value.getName().isBlank() &&
			value.getDateOfBirth() != null &&
			value.getCats() != null;
	}
}
