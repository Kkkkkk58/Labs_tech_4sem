package ru.kslacker.cats.microservices.restapi.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.kslacker.cats.microservices.restapi.validation.validators.PastOrPresentUpdateDateValidator;
import java.lang.annotation.*;

@Constraint(validatedBy = PastOrPresentUpdateDateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PastOrPresentUpdateDate {

	String message() default "Date is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
