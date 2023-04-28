package ru.kslacker.cats.services.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.kslacker.cats.services.validation.annotations.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	// TODO
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return true;
	}
}
