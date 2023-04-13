package ru.kslacker.cats.services.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CatOwnerValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCatOwner {
	String message() default "Cat owner data is invalid";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
