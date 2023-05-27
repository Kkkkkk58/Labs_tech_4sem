package ru.kslacker.cats.microservices.restapi.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.kslacker.cats.microservices.restapi.validation.annotations.UpdateName;

public class UpdateNameValidator implements ConstraintValidator<UpdateName, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || !value.isBlank();
	}
}
