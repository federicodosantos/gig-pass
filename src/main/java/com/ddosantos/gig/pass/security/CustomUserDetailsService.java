package com.ddosantos.gig.pass.security;

import com.ddosantos.gig.pass.entity.User;
import com.ddosantos.gig.pass.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailParam) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(emailParam));
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with email: "+ emailParam+ " not found");
        }
        String email = user.get().getEmail();
        String password = user.get().getPassword();
        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                Collections.emptyList());
    }
}
