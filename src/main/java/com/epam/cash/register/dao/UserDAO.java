package com.epam.cash.register.dao;

import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    List<User> findAll(Connection connection) throws SQLException;

    User findByUsername(Connection connection, String username) throws SQLException,UserNotFoundException;

    User findById(Connection connection, long id) throws SQLException, UserNotFoundException;

    void insert(Connection connection, User user) throws SQLException;

    void update(Connection connection, User user) throws SQLException;

    void insert(Connection connection, User... users) throws SQLException;

    void delete(Connection connection, String username) throws SQLException;

    void delete(Connection connection, long id) throws SQLException;

    void delete(Connection connection, String... usernames) throws SQLException;

    void delete(Connection connection, long... ids) throws SQLException;

    boolean isUserExists(Connection connection, String username) throws SQLException;

    boolean isUserExists(Connection connection, long userID) throws SQLException;
}
