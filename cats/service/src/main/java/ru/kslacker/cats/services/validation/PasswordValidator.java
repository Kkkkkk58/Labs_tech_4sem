package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	// TODO
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return true;
	}
}
