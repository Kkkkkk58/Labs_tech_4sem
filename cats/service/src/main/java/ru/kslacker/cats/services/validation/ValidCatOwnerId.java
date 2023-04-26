package ru.kslacker.cats.services.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CatOwnerIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidCatOwnerId {

	String message() default "Cat owner getId is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
