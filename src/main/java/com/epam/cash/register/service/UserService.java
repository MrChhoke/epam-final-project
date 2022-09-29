package com.epam.cash.register.service;

import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.RegisteredUserException;

import java.io.IOException;
import java.util.List;

public interface UserService {
    boolean isUserExists(User user);

    boolean isUserExists(String name);

    void registerUser(User user) throws RegisteredUserException;

    User findUserByName(String name) throws IOException;

    List<User> findAll() throws IOException;

    void updateUser(User user) throws IOException;

    void delete(long... id) throws IOException;

    void delete(String username) throws IOException;
}
