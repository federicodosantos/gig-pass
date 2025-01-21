package com.ddosantos.gig.pass;

import com.ddosantos.gig.pass.dto.request.UserLoginDTO;
import com.ddosantos.gig.pass.dto.request.UserRegistrationDTO;
import com.ddosantos.gig.pass.dto.response.UserLoginResponseDTO;
import com.ddosantos.gig.pass.dto.response.UserRegistrationResponseDTO;
import com.ddosantos.gig.pass.entity.User;
import com.ddosantos.gig.pass.exception.EmailAlreadyExistException;
import com.ddosantos.gig.pass.exception.ResourceNotFoundException;
import com.ddosantos.gig.pass.exception.UnauthorizedException;
import com.ddosantos.gig.pass.repository.UserRepository;
import com.ddosantos.gig.pass.security.TokenProvider;
import com.ddosantos.gig.pass.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        verify(userRepository, times(1)).createUser(any(User.class));

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals("Successfully registered new user", responseDTO.getMessage());
        verify(passwordEncoder).encode(dto.getPassword());
    }

    @Test
    void testRegister_ShouldReturnEmailAlreadyException_WhenUserEmailIsAlreadyExist() {
        UserRegistrationDTO dto = createUserRegistrationDTO();

        User existingUser = new User();
        existingUser.setEmail(dto.getEmail());

        when(userRepository.findUserByEmail(dto.getEmail())).thenReturn(existingUser);

        Assertions.assertThrows(EmailAlreadyExistException.class, () -> {
            userService.register(dto);
        });

        verify(userRepository, never()).createUser(any(User.class));
    }

    @Test
    void testLogin_ShouldReturnLoginResponseDTO_WhenUserLoginSuccessfully() {
        UserLoginDTO loginDTO = createUserLoginDTO();
        User user = new User();
        user.setEmail(loginDTO.getEmail());
        user.setPassword(loginDTO.getPassword());

        when(userRepository.findUserByEmail(loginDTO.getEmail())).thenReturn(user);

        Authentication mockAuthenticate = mock(Authentication.class);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(mockAuthenticate);

        when(tokenProvider.createToken(mockAuthenticate)).thenReturn("jwt-token");

        UserLoginResponseDTO responseDTO = userService.login(loginDTO);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals("jwt-token", responseDTO.getToken());
    }

    @Test
    void testLogin_ShouldReturnResourceNotFoundException_WhenUserEmailNotExist() {
        UserLoginDTO loginDTO = createUserLoginDTO();

        when(userRepository.findUserByEmail(loginDTO.getEmail())).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.login(loginDTO);
        });
    }

    @Test
    void testLogin_ShouldReturnUnauthorizedException_WhenUserCredentialsIsInvalid() {
        UserLoginDTO loginDTO = createUserLoginDTO();
        User user = new User();
        user.setEmail(loginDTO.getEmail());
        user.setPassword("correct-password");

        when(userRepository.findUserByEmail(loginDTO.getEmail())).thenReturn(user);

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(Authentication.class))).
                thenThrow(new BadCredentialsException("Invalid email or password"));

        UnauthorizedException exception = Assertions.assertThrows(UnauthorizedException.class, () -> {
            userService.login(loginDTO);
        });

        Assertions.assertEquals("Invalid email or password", exception.getMessage());
    }

    private UserRegistrationDTO createUserRegistrationDTO() {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setName(NAME);
        userDTO.setEmail(EMAIL);
        userDTO.setPassword(PASSWORD);

        return userDTO;
    }

    private UserLoginDTO createUserLoginDTO() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail(EMAIL);
        loginDTO.setPassword(PASSWORD);

        return loginDTO;
    }
}
