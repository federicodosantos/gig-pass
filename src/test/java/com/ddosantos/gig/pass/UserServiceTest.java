package com.ddosantos.gig.pass;

import com.ddosantos.gig.pass.dto.request.UserRegistrationDTO;
import com.ddosantos.gig.pass.dto.response.UserRegistrationResponseDTO;
import com.ddosantos.gig.pass.entity.User;
import com.ddosantos.gig.pass.repository.UserRepository;
import com.ddosantos.gig.pass.security.TokenProvider;
import com.ddosantos.gig.pass.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String NAME = "riko";
    private static final String EMAIL = "riko@gmail.com";
    private static final String PASSWORD = "rahasia123";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegister_ShouldReturnSuccessMessage_WhenUserIsRegisteredSuccessfully() {
        UserRegistrationDTO dto = createUserRegistrationDTO();

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword("hashedPassword");

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");

        UserRegistrationResponseDTO responseDTO = userService.register(dto);
        verify(userRepository, Mockito.times(1)).createUser(any(User.class));

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals("Successfully registered new user", responseDTO.getMessage());
        verify(passwordEncoder).encode(dto.getPassword());
    }

    private UserRegistrationDTO createUserRegistrationDTO() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setName(NAME);
        userDTO.setEmail(EMAIL);
        userDTO.setPassword(PASSWORD);

        return userDTO;
    }
}
