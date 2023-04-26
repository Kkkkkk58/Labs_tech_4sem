package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class UpdateDateValidator implements ConstraintValidator<UpdateDate, LocalDate> {

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		LocalDate localDate = LocalDate.now();
		return value == null || localDate.isAfter(value) || localDate.isEqual(value);
	}
}
