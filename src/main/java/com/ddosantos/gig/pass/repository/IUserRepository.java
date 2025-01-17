package com.ddosantos.gig.pass.repository;

import com.ddosantos.gig.pass.entity.User;

public interface IUserRepository {
    void createUser(User user);
    User findUserByEmail(String email);
}
