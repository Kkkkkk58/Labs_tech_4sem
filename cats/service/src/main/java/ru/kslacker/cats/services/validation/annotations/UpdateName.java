package ru.kslacker.cats.services.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.kslacker.cats.services.validation.validators.UpdateNameValidator;

@Constraint(validatedBy = UpdateNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UpdateName {

	String message() default "Name is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
