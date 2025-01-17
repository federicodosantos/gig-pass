package com.ddosantos.gig.pass.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserRegistrationResponseDTO {
    private UUID id;
    private String name;
    private String message;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
