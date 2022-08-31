package com.epam.cash.register.dao;

import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostqreSQLUserDAO implements UserDAO {

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_LOGIN = "login";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_ROLE = "role";

    private final static String GET_ALL_USERS = "SELECT * FROM users";

    @Override
    public List<User> findAll(Connection connection) throws SQLException {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = null;
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(GET_ALL_USERS);
            while (resultSet.next()) {
                users.add(createUser(resultSet));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return users;
    }

    @Override
    public User findByUsername(Connection connection, String username) throws UserNotFoundException, SQLException {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login = ?")) {
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.next()) {
                return createUser(resultSet);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        throw new UserNotFoundException("No user with this name was found");
    }

    @Override
    public User findById(Connection connection, long id) throws UserNotFoundException, SQLException {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?")) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.next()) {
                return createUser(resultSet);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        throw new UserNotFoundException("No user with this id was found");
    }

    @Override
    public void insert(Connection connection, User user) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(login, password, role) VALUES (?,?,cast(? AS USER_ROLES))")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString().toLowerCase(Locale.ROOT));
            preparedStatement.execute();
        }
    }

    @Override
    public void update(Connection connection, User user) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET login = ?, password = ?, role = ? WHERE user_id = ?")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void insert(Connection connection, User... users) throws SQLException {
        for (User user : users) {
            insert(connection, user);
        }
    }

    @Override
    public void delete(Connection connection, String username) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE login = ?")) {
            preparedStatement.setString(1,username);
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(Connection connection, String... usernames) {
        for(String username : usernames){
            delete(connection, usernames);
        }
    }

    @Override
    public void delete(Connection connection, long... ids) throws SQLException {
        for(long id : ids){
            delete(connection, id);
        }
    }

    @Override
    public boolean isUserExists(Connection connection, String username) throws SQLException {
        try {
            findByUsername(connection, username);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isUserExists(Connection connection, long userID) throws SQLException {
        try {
            findById(connection, userID);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }


    private User createUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong(COLUMN_USER_ID));
        user.setUsername(resultSet.getString(COLUMN_USER_LOGIN));
        user.setPassword(resultSet.getString(COLUMN_USER_PASSWORD));
        user.setRole(User.Role.valueOf(resultSet.getString(COLUMN_USER_ROLE)));
        return user;
    }
}
