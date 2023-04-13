package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.kslacker.cats.dataaccess.entities.Cat;

@Component
public class CatValidator implements ConstraintValidator<ValidCat, Cat> {

	@Override
	public void initialize(ValidCat constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Cat value, ConstraintValidatorContext context) {
		return !value.getName().isBlank() &&
			value.getFriends() != null &&
			value.getFurColor() != null
			&& value.getDateOfBirth() != null;
	}
}
