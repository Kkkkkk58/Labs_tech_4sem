package ru.kslacker.cats.microservices.restapi.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import ru.kslacker.cats.microservices.restapi.validation.annotations.PastOrPresentUpdateDate;

public class PastOrPresentUpdateDateValidator implements
	ConstraintValidator<PastOrPresentUpdateDate, LocalDate> {

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		LocalDate localDate = LocalDate.now();
		return value == null || localDate.isAfter(value) || localDate.isEqual(value);
	}
}
