package com.ddosantos.gig.pass.security;

import com.ddosantos.gig.pass.entity.User;
import com.ddosantos.gig.pass.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Collections;

@AllArgsConstructor
@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String emailParam) throws UsernameNotFoundException {
        log.info("masuk load user by email : {}", emailParam);

        User user = userRepository.findUserByEmail(emailParam);
        if (user == null) {
            log.info("user not found: {}", emailParam);
            throw new UsernameNotFoundException("User with email: "+ emailParam+ " not found");
        }

        log.info("user found: {}", emailParam);
        String email = user.getEmail();
        String password = user.getPassword();
        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                Collections.emptyList());
    }
}
