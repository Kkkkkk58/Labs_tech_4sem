package ru.kslacker.cats.microservices.users.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.microservices.users.dataaccess.repositories.api.UserRepository;
import ru.kslacker.cats.microservices.users.validation.annotations.ValidUserId;

public class UserIdValidator implements ConstraintValidator<ValidUserId, Long> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return userRepository.existsById(value);
	}
}
