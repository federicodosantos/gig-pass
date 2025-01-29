package com.ddosantos.gig.pass;

import com.ddosantos.gig.pass.configuration.SecurityConfig;
import com.ddosantos.gig.pass.controller.UserController;
import com.ddosantos.gig.pass.dto.request.UserLoginDTO;
import com.ddosantos.gig.pass.dto.request.UserRegistrationDTO;
import com.ddosantos.gig.pass.dto.response.UserLoginResponseDTO;
import com.ddosantos.gig.pass.dto.response.UserRegistrationResponseDTO;
import com.ddosantos.gig.pass.entity.User;
import com.ddosantos.gig.pass.exception.EmailAlreadyExistException;
import com.ddosantos.gig.pass.exception.ResourceNotFoundException;
import com.ddosantos.gig.pass.exception.UnauthorizedException;
import com.ddosantos.gig.pass.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {
    private static final String NAME = "riko";
    private static final String EMAIL = "riko@gmail.com";
    private static final String PASSWORD = "rahasia123";
    private static final String endpointRegister = "/api/users/register";
    private static final String endpointLogin = "/api/users/login";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Test
    void testRegister_ShouldReturnHttpStatusCreated_WhenUserSuccessfullyRegistered() throws Exception {
        UserRegistrationDTO registrationDTO = createUserRegistrationDTO(user);

        UserRegistrationResponseDTO responseDTO =
                new UserRegistrationResponseDTO(
                        user.getId(),
                        user.getName(),
                        "Successfully registered new user",
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                );

        when(userService.register(any(UserRegistrationDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post(endpointRegister)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId().toString()))
                .andExpect(jsonPath("$.name").value(responseDTO.getName()))
                .andExpect(jsonPath("$.message").value(responseDTO.getMessage()))
                .andExpect(jsonPath("$.createdAt").value(responseDTO.getCreatedAt()))
                .andExpect(jsonPath("$.updatedAt").value(responseDTO.getUpdatedAt()));

    }

    @Test
    void testRegister_ShouldReturnStatusConflict_WhenEmailAlreadyExists() throws Exception {
        UserRegistrationDTO registrationDTO = createUserRegistrationDTO(user);

        String errorMessage = "This email: " + registrationDTO.getEmail() + " already exists";

        when(userService.register(any(UserRegistrationDTO.class))).
                thenThrow(new EmailAlreadyExistException(errorMessage));

        mockMvc.perform(post(endpointRegister)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.timestamp").value(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .format(LocalDateTime.now())));
    }

    @Test
    void testLogin_ShouldReturnSuccess_WhenUserLoginSuccessfully() throws Exception {
        UserLoginDTO loginDTO = createUserLoginDTO(user);

        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO(
                loginDTO.getEmail(),
                loginDTO.getPassword()
        );

        when(userService.login(any(UserLoginDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post(endpointLogin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(responseDTO.getEmail()))
                .andExpect(jsonPath("$.token").value(responseDTO.getToken()));
    }

    @Test
    void testLogin_ShouldReturnResourceNotFound_WhenUserEmailNotFound() throws Exception {
        UserLoginDTO loginDTO = createUserLoginDTO(user);

        String errorMessage = "Email: " + loginDTO.getEmail() + " not found";

        when(userService.login(any(UserLoginDTO.class)))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        mockMvc.perform(post(endpointLogin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.timestamp").value(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .format(LocalDateTime.now())));
    }

    @Test
    void testLogin_ShouldReturnUnauthorized_WhenUserPasswordIsWrong() throws Exception {
        UserLoginDTO loginDTO = createUserLoginDTO(user);

        String errorMessage = "Invalid email or password";

        when(userService.login(any(UserLoginDTO.class)))
                .thenThrow(new UnauthorizedException(errorMessage));

        mockMvc.perform(post(endpointLogin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.timestamp").value(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .format(LocalDateTime.now())));
    }

    private UserRegistrationDTO createUserRegistrationDTO(User user) {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setConfirmPassword(user.getPassword());

        return userDTO;
    }

    private UserLoginDTO createUserLoginDTO(User user) {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail(user.getEmail());
        loginDTO.setPassword(user.getPassword());

        return loginDTO;
    }
}
