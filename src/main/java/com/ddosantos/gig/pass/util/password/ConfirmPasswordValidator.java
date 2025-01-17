package com.ddosantos.gig.pass.util.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPasswordValidator {
    String message() default "Password and Confirm Password are not the same.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


