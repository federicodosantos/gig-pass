package com.ddosantos.gig.pass.dto.request;

import com.ddosantos.gig.pass.util.password.ConfirmPasswordValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfirmPasswordValidator
public class UserRegistrationDTO {
    @NotNull
    @Size(min = 3, max = 100, message = "name must be at least 3 and maximum 100")
    private String name;

    @NotNull
    @Email(message = "Email must be valid")
    private String email;

    @NotNull
    @Size(min = 8, message = "Password must be at least 8 word")
    private String password;

    @NotNull
    private String confirmPassword;
}
