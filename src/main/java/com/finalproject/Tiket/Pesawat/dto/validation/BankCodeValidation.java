package com.finalproject.Tiket.Pesawat.dto.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = BankCodeValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface BankCodeValidation {
    String message() default "Invalid bank code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}