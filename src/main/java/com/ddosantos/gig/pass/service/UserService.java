package com.ddosantos.gig.pass.service;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public UserRegistrationResponseDTO register(UserRegistrationDTO dto) {
        User exists = userRepository.findUserByEmail(dto.getEmail());
        if (exists != null) {
            throw new EmailAlreadyExistException("This email: " + exists.getEmail() + " already exists");
        }

        User user = new User();
        String hashpw = passwordEncoder.encode(dto.getPassword());

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(hashpw);
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        userRepository.createUser(user);

        return new UserRegistrationResponseDTO(
                user.getId(),
                user.getName(),
                "Successfully registered new user",
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public UserLoginResponseDTO login(UserLoginDTO loginDTO) {
        try {
            // Check if email exists in the database
            if (userRepository.findUserByEmail(loginDTO.getEmail()) == null) {
                throw new ResourceNotFoundException("Email: " + loginDTO.getEmail() + " not found");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    ));

            String jwtToken = tokenProvider.createToken(authentication);

            return new UserLoginResponseDTO(
                    authentication.getName(),
                    jwtToken
            );
        } catch (BadCredentialsException ex) {
            log.error("Bad credentials for email: {}", loginDTO.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        } catch (ResourceNotFoundException ex) {
            log.error("Resource not found: {}", ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

}
