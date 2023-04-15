package ru.kslacker.cats.presentation.handlers;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springdoc.api.ErrorMessage;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kslacker.cats.common.exceptions.CatsException;
import ru.kslacker.cats.dataaccess.exceptions.CatException;
import ru.kslacker.cats.dataaccess.exceptions.CatOwnerException;
import ru.kslacker.cats.services.exceptions.EntityException;
import ru.kslacker.cats.presentation.responses.ValidationErrorResponse;
import ru.kslacker.cats.presentation.responses.Violation;

@RestControllerAdvice
public class RestControllerExceptionHandler {

	@ExceptionHandler(EntityException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> entityNotFoundException(EntityException exception) {

		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(new ErrorMessage(exception.getMessage()));
	}

	@ExceptionHandler({CatException.class, CatOwnerException.class})
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<ErrorMessage> domainException(CatsException exception) {

		return ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(new ErrorMessage(exception.getMessage()));
	}


	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ValidationErrorResponse> constraintValidationException(ConstraintViolationException exception) {

		List<Violation> violations = exception.getConstraintViolations().stream().map(
			constraintViolation -> new Violation(
				constraintViolation.getPropertyPath().toString(),
				constraintViolation.getMessage()
			)
		).toList();

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ValidationErrorResponse(violations));
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ValidationErrorResponse> methodArgumentsValidationsException(
		MethodArgumentNotValidException exception) {

		List<Violation> violations = exception.getBindingResult().getFieldErrors().stream().map(
			fieldError -> new Violation(
				fieldError.getField(),
				fieldError.getDefaultMessage()
			)
		).toList();

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ValidationErrorResponse(violations));
	}

	@ExceptionHandler(PropertyReferenceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorMessage> sortOptionNotFound(PropertyReferenceException exception) {

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorMessage(exception.getMessage()));
	}
}
