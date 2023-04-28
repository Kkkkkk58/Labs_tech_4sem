package ru.kslacker.cats.services.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.kslacker.cats.services.validation.annotations.UpdateName;

public class UpdateNameValidator implements ConstraintValidator<UpdateName, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || !value.isBlank();
	}
}
