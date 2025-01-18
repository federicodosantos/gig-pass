package com.ddosantos.gig.pass.util.password;

import com.ddosantos.gig.pass.dto.request.UserRegistrationDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PasswordMatchValidator implements ConstraintValidator<ConfirmPasswordValidator, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        if (o instanceof UserRegistrationDTO dto) {
            if (!Objects.equals(dto.getPassword(), dto.getConfirmPassword())) {
                context.buildConstraintViolationWithTemplate("Password and confirm password must match")
                        .addPropertyNode("confirmPassword")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
