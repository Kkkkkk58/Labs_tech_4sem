package ru.kslacker.cats.microservices.users.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.kslacker.cats.microservices.users.validation.validators.UserIdValidator;
import java.lang.annotation.*;

@Constraint(validatedBy = UserIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidUserId {

	String message() default "User id is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
