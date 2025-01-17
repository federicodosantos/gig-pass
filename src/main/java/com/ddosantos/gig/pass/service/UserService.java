package com.ddosantos.gig.pass.service;

import com.ddosantos.gig.pass.dto.request.UserLoginDTO;
import com.ddosantos.gig.pass.dto.request.UserRegistrationDTO;
import com.ddosantos.gig.pass.dto.response.UserLoginResponseDTO;
import com.ddosantos.gig.pass.dto.response.UserRegistrationResponseDTO;
import com.ddosantos.gig.pass.entity.User;
import com.ddosantos.gig.pass.repository.UserRepository;
import com.ddosantos.gig.pass.security.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService implements IUserService {
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

    @Override
    public UserRegistrationResponseDTO register(UserRegistrationDTO dto) {
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

    @Override
    public UserLoginResponseDTO login(UserLoginDTO loginDTO) {
        try {
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
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
