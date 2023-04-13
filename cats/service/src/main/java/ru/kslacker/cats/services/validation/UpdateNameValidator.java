package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateNameValidator implements ConstraintValidator<ValidUpdateName, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || !value.isBlank();
	}
}
