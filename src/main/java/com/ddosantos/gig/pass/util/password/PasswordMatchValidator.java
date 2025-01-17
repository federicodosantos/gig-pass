package com.ddosantos.gig.pass.util.password;

import com.ddosantos.gig.pass.dto.request.UserRegistrationDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PasswordMatchValidator implements ConstraintValidator<ConfirmPasswordValidator, Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof UserRegistrationDTO dto) {
            return Objects.equals(dto.getPassword(), dto.getConfirmPassword());
        }

        return false;
    }
}
