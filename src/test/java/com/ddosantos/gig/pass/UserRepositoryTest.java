package com.ddosantos.gig.pass;

import com.ddosantos.gig.pass.entity.User;
import com.ddosantos.gig.pass.repository.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(UserRepository.class)
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback
    void testCreateUser() {
        User user = createUser();

        Assertions.assertNotNull(user.getId());

        User savedUser = userRepository.findUserByEmail(user.getEmail());
        Assertions.assertNotNull(savedUser);
        Assertions.assertSame(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void testFindUserByEmail() {
        User user = createUser();

        User savedUser = userRepository.findUserByEmail(user.getEmail());
        Assertions.assertNotNull(savedUser);
        Assertions.assertSame(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void testFindUserByEmailNotFound() {
        User notFoundUser = userRepository.findUserByEmail("notfound@gmail.com");

        Assertions.assertNull(notFoundUser);
    }

    private User createUser() {
        String name = "riko";
        String email = "riko@gmail.com";
        String password = "rahasia123";

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.createUser(user);

        return user;
    }
}
