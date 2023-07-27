package com.practice.security.services;

import com.practice.security.model.dto.User;

public interface UserService {

    void saveVerificationTokenForUser(String token, User user);

    User registerUser(User user);

    String validateVerificationToken(String token);
}
