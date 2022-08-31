package com.epam.cash.register.dao;

import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.ProductNotFoundException;
import com.epam.cash.register.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ProductDAO {

    List<Product> findAll(Connection connection) throws SQLException;

    Product findById(Connection connection, long id) throws SQLException, ProductNotFoundException;

    List<Product> findById(Connection connection, long... id) throws SQLException;

    List<Product> findById(Connection connection, List<Long> id) throws SQLException;

    Product findByCode(Connection connection, String code) throws SQLException, ProductNotFoundException;

    Product findByUkrainianTitle(Connection connection, String ukrainianTitle) throws SQLException, ProductNotFoundException;

    Product findByEnglishTitle(Connection connection, String englishTitle) throws SQLException, ProductNotFoundException;

    List<Product> findAllOutStock(Connection connection) throws SQLException;

    List<Product> findAllBetweenDate(Connection connection, Date from, Date to) throws SQLException;

    List<Product> findAllBetweenPrice(Connection connection, double from, double to) throws SQLException;

    List<Product> findAllAddedByUser(Connection connection, User user) throws SQLException, UserNotFoundException;

    List<Product> findAllAddedByUser(Connection connection, String username) throws SQLException, UserNotFoundException;

    void insert(Connection connection, Product product) throws SQLException;

    void update(Connection connection, Product product) throws SQLException;

    void delete(Connection connection, long product_id) throws SQLException;

    void delete(Connection connection, String code) throws SQLException;
}
