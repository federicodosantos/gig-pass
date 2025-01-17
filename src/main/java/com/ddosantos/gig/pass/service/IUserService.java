package com.ddosantos.gig.pass.service;

import com.ddosantos.gig.pass.dto.request.UserLoginDTO;
import com.ddosantos.gig.pass.dto.request.UserRegistrationDTO;
import com.ddosantos.gig.pass.dto.response.UserLoginResponseDTO;
import com.ddosantos.gig.pass.dto.response.UserRegistrationResponseDTO;

public interface IUserService {
    UserRegistrationResponseDTO register(UserRegistrationDTO dto);
    UserLoginResponseDTO login(UserLoginDTO loginDTO);
}
