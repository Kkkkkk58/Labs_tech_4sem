package ru.kslacker.cats.services.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.kslacker.cats.services.validation.annotations.UpdateDate;

import java.time.LocalDate;

public class UpdateDateValidator implements ConstraintValidator<UpdateDate, LocalDate> {

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		LocalDate localDate = LocalDate.now();
		return value == null || localDate.isAfter(value) || localDate.isEqual(value);
	}
}
