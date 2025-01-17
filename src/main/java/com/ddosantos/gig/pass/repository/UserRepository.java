package com.ddosantos.gig.pass.repository;

import com.ddosantos.gig.pass.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository implements IUserRepository{
    private EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void createUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.email=:email",
                User.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}