package com.ddosantos.gig.pass.controller;

import com.ddosantos.gig.pass.dto.request.UserLoginDTO;
import com.ddosantos.gig.pass.dto.request.UserRegistrationDTO;
import com.ddosantos.gig.pass.dto.response.UserLoginResponseDTO;
import com.ddosantos.gig.pass.dto.response.UserRegistrationResponseDTO;
import com.ddosantos.gig.pass.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserRegistrationResponseDTO> register(@RequestBody @Valid UserRegistrationDTO registrationDTO) {
        return new ResponseEntity<>(userService.register(registrationDTO), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginDTO loginDTO) {
        return ResponseEntity.ok(userService.login(loginDTO));
    }
}
