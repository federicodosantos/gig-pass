package com.ddosantos.gig.pass.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponseDTO {
    private String email;
    private String token;
}
