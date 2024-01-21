package com.playko.userservice.service;

import com.playko.userservice.entities.User;

import java.util.Optional;

public interface IUserService {
    void saveUser(User user);
    Optional<User> getUserByEmail(String email);
}
