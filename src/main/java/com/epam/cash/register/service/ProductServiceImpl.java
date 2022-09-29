package com.epam.cash.register.service;

import com.epam.cash.register.dao.PostqreSQLProductDAO;
import com.epam.cash.register.dao.ProductDAO;
import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private static final Logger log = LogManager.getLogger(ProductServiceImpl.class);

    private ProductDAO productDAO;

    public ProductServiceImpl() {
        productDAO = new PostqreSQLProductDAO();
    }

    @Override
    public List<Product> findAll() throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findAll(connection);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public Product findById(long id) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findById(connection, id);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Product> findById(long... id) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findById(connection, id);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Product> findById(List<Long> id) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findById(connection, id);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public Product findByCode(String code) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findByCode(connection, code);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public Product findByUkrainianTitle(String ukrainianTitle) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findByUkrainianTitle(connection, ukrainianTitle);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public Product findByEnglishTitle(String englishTitle) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findByEnglishTitle(connection, englishTitle);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Product> findAllOutStock() throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findAllOutStock(connection);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Product> findAllBetweenDate(Date from, Date to) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findAllBetweenDate(connection, from, to);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Product> findAllBetweenPrice(double from, double to) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findAllBetweenPrice(connection, from, to);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Product> findAllAddedByUser(User user) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findAllAddedByUser(connection, user);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Product> findAllAddedByUser(String username) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return productDAO.findAllAddedByUser(connection, username);
        } catch (SQLException | NamingException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public void insert(Product product) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            productDAO.insert(connection, product);
            connection.commit();
        } catch (SQLException | NamingException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(Product product) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            productDAO.update(connection, product);
            connection.commit();
        } catch (SQLException | NamingException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void delete(long product_id) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            productDAO.delete(connection, product_id);
            connection.commit();
        } catch (SQLException | NamingException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void delete(String code) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            productDAO.delete(connection, code);
            connection.commit();
        } catch (SQLException | NamingException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
