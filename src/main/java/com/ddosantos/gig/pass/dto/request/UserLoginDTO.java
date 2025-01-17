package com.ddosantos.gig.pass.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
