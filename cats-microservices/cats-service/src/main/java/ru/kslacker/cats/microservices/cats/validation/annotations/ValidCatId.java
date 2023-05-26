package ru.kslacker.cats.microservices.cats.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.kslacker.cats.microservices.cats.validation.validators.CatIdValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CatIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidCatId {

	String message() default "Cat id is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
