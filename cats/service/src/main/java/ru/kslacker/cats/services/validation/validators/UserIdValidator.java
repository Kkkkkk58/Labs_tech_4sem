package ru.kslacker.cats.services.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.dataaccess.repositories.UserRepository;
import ru.kslacker.cats.services.validation.annotations.ValidUserId;

public class UserIdValidator implements ConstraintValidator<ValidUserId, Long> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return userRepository.existsById(value);
	}
}
