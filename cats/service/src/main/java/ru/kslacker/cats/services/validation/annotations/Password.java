package ru.kslacker.cats.services.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.kslacker.cats.services.validation.validators.PasswordValidator;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Password {

	String message() default "Password format is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
