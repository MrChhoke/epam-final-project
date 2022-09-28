package com.epam.cash.register.service;

import com.epam.cash.register.dao.PostqreSQLUserDAO;
import com.epam.cash.register.dao.UserDAO;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.RegisteredUserException;
import com.epam.cash.register.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private UserDAO userDAO;

    public UserServiceImpl() {
        userDAO = new PostqreSQLUserDAO();
    }

    public UserServiceImpl(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public boolean isUserExists(User user) {
        try (Connection connection = DBUtil.getConnection()) {
            return userDAO.isUserExists(connection, user.getUsername()) || userDAO.isUserExists(connection, user.getId());
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isUserExists(String name) {
        try (Connection connection = DBUtil.getConnection()) {
            return userDAO.isUserExists(connection, name);
        } catch (SQLException | NamingException e) {
            log.error("Something is wrong: ", e);
            return false;
        }
    }

    @Override
    public void registerUser(User user) throws RegisteredUserException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            if (userDAO.isUserExists(connection, user.getUsername()) ||
                    userDAO.isUserExists(connection, user.getId())) {
                throw new RegisteredUserException("The username is already exist");
            }
            connection.setAutoCommit(false);
            userDAO.insert(connection, user);
            connection.commit();
        } catch (SQLException | NamingException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            log.error("Something is wrong: ", e);
        }
    }

    @Override
    public User findUserByName(String name) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return userDAO.findByUsername(connection, name);
        } catch (SQLException | NamingException exp) {
            log.error("Something is wrong: ", exp);
            throw new IOException("Smth is wrong", exp);
        }
    }

    @Override
    public List<User> findAll() throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return userDAO.findAll(connection);
        } catch (SQLException | NamingException exp) {
            log.error("Something is wrong: ", exp);
            throw new IOException("Smth is wrong", exp);
        }
    }

    @Override
    public void updateUser(User user) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            userDAO.update(connection, user);
            connection.commit();
        } catch (SQLException | NamingException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            log.error("Something is wrong: ", exp);
            throw new IOException("Smth is wrong", exp);
        }
    }

    @Override
    public void delete(long... id) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            userDAO.delete(connection, id);
            connection.commit();
        } catch (SQLException | NamingException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            log.error("Something is wrong: ", exp);
            throw new IOException("Smth is wrong", exp);
        }
    }

    @Override
    public void delete(String username) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            userDAO.delete(connection, username);
            connection.commit();
        } catch (SQLException | NamingException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            log.error("Something is wrong: ", exp);
            throw new IOException("Smth is wrong", exp);
        }
    }
}
