package ru.kslacker.cats.presentation.handlers;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kslacker.cats.common.exceptions.CatsException;
import ru.kslacker.cats.dataaccess.exceptions.CatException;
import ru.kslacker.cats.dataaccess.exceptions.CatOwnerException;
import ru.kslacker.cats.dataaccess.exceptions.EntityException;
import ru.kslacker.cats.presentation.responses.ValidationErrorResponse;
import ru.kslacker.cats.presentation.responses.Violation;


// TODO add err page and bare ControllerAdvice
@RestControllerAdvice
public class RestControllerExceptionHandler {

	@ExceptionHandler(EntityException.class)
	public ResponseEntity<ErrorMessage> entityNotFoundException(EntityException exception) {
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(new ErrorMessage(exception.getMessage()));
	}

	@ExceptionHandler({CatException.class, CatOwnerException.class})
	public ResponseEntity<ErrorMessage> domainException(CatsException exception) {
		return ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(new ErrorMessage(exception.getMessage()));
	}


	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationErrorResponse> constraintValidationException(
		ConstraintViolationException exception) {
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
}
