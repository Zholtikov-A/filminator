package com.zholtikov.filminator.userservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsAfterCinemaBirthdayValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsAfterCinemaBirthday {
    String message() default "Invalid release date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}