package ru.kslacker.cats.services.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ru.kslacker.cats.services.validation.validators.CatIdValidator;

@Constraint(validatedBy = CatIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidCatId {

	String message() default "Cat id is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
