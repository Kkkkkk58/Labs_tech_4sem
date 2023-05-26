package ru.kslacker.cats.microservices.catowners.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.kslacker.cats.microservices.catowners.validation.validators.CatOwnerIdValidator;
import java.lang.annotation.*;

@Constraint(validatedBy = CatOwnerIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidCatOwnerId {

	String message() default "Cat owner id is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
