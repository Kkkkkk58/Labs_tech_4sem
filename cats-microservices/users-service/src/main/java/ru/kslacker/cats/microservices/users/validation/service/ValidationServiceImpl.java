package ru.kslacker.cats.microservices.users.validation.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kslacker.cats.microservices.users.validation.service.api.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService {

	private final Validator validator;

	@Autowired
	public ValidationServiceImpl(Validator validator) {
		this.validator = validator;
	}

	@Override
	public <T> void validate(T t) {
		Set<ConstraintViolation<T>> violations = validator.validate(t);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}
}
