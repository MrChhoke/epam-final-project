package com.epam.cash.register.service;

import com.epam.cash.register.dao.PostqreSQLUserDAO;
import com.epam.cash.register.dao.UserDAO;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.RegisteredUserException;
import com.epam.cash.register.exception.UserNotFoundException;
import com.epam.cash.register.util.DBUtil;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public UserServiceImpl() {
        userDAO = new PostqreSQLUserDAO();
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
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void registerUser(User user) throws RegisteredUserException {
        try (Connection connection = DBUtil.getConnection()) {
            if (userDAO.isUserExists(connection, user.getUsername()) ||
                    userDAO.isUserExists(connection, user.getId())) {
                throw new RegisteredUserException("The username is already exist");
            }
            userDAO.insert(connection, user);
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findUserByName(String name) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return userDAO.findByUsername(connection, name);
        } catch (SQLException | NamingException exp) {
            exp.printStackTrace();
            throw new IOException("Smth is wrong", exp);
        }
    }

    @Override
    public List<User> findAll() throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return userDAO.findAll(connection);
        } catch (SQLException | NamingException exp) {
            exp.printStackTrace();
            throw new IOException("Smth is wrong", exp);
        }
    }

    @Override
    public void updateUser(User user) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            userDAO.update(connection, user);
        }catch (SQLException | NamingException exp) {
            exp.printStackTrace();
            throw new IOException("Smth is wrong", exp);
        }
    }

    @Override
    public void delete(long... id) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            userDAO.delete(connection, id);
        }catch (SQLException | NamingException exp) {
            exp.printStackTrace();
            throw new IOException("Smth is wrong", exp);
        }
    }

    @Override
    public void delete(String username) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            userDAO.delete(connection, username);
        }catch (SQLException | NamingException exp) {
            exp.printStackTrace();
            throw new IOException("Smth is wrong", exp);
        }
    }
}
