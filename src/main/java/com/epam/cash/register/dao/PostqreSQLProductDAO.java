package com.epam.cash.register.dao;

import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.ProductNotFoundException;
import com.epam.cash.register.exception.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PostqreSQLProductDAO implements ProductDAO {

    private static final String COLUMN_ID_PRODUCT = "product_id";
    private static final String COLUMN_UKRAINIAN_TITLE_PRODUCT = "title_ukr";
    private static final String COLUMN_ENGLISH_TITLE_PRODUCT = "title_eng";
    private static final String COLUMN_PRICE_PRODUCT = "price";
    private static final String COLUMN_CODE_PRODUCT = "code";
    private static final String COLUMN_QUANTITY_PRODUCT = "quantity";
    private static final String COLUMN_DATE_CREATION_PRODUCT = "date_creation";
    private static final String COLUMN_USER_CREATOR_ID_PRODUCT = "date_creation";

    private static final String GET_ALL_PRODUCT_SQL = "SELECT * FROM products";
    private static final String GET_ALL_STOCK_OF_OUT_PRODUCT_SQL = "SELECT * FROM products WHERE quantity = 0";

    private final UserDAO userDAO = new PostqreSQLUserDAO();

    @Override
    public List<Product> findAll(Connection connection) throws SQLException {
        List<Product> products = new ArrayList<>(20);
        ResultSet resultSet = null;
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(GET_ALL_PRODUCT_SQL);
            while (resultSet.next()) {
                products.add(createProduct(resultSet, connection));
            }
            return products;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    @Override
    public Product findById(Connection connection, long id) throws SQLException, ProductNotFoundException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE product_id = ?")) {
            statement.setString(1, String.valueOf(id));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createProduct(resultSet, connection);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }

        throw new ProductNotFoundException("No product with this id was found");
    }

    @Override
    public List<Product> findById(Connection connection, long... id) throws SQLException {
        String values = Arrays.stream(id)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        List<Product> products = new ArrayList<>(100);
        ResultSet rs = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products WHERE product_id IN(?)")) {
            preparedStatement.setString(1,values);
            rs = preparedStatement.executeQuery();
            products.add(createProduct(rs,connection));
        }finally {
            if(rs != null) {
                rs.close();
            }
        }
        return products;
    }

    @Override
    public List<Product> findById(Connection connection, List<Long> id) throws SQLException {
        String values = id.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        List<Product> products = new ArrayList<>(100);
        ResultSet rs = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products WHERE product_id IN(?)")) {
            preparedStatement.setString(1,values);
            rs = preparedStatement.executeQuery();
            products.add(createProduct(rs,connection));
        }finally {
            if(rs != null) {
                rs.close();
            }
        }
        return products;
    }

    @Override
    public Product findByCode(Connection connection, String code) throws SQLException, ProductNotFoundException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE code = ?")) {
            statement.setString(1, code);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createProduct(resultSet, connection);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        throw new ProductNotFoundException("No product with this code was found");
    }

    @Override
    public Product findByUkrainianTitle(Connection connection, String ukrainianTitle) throws SQLException, ProductNotFoundException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE title_ukr = ?")) {
            statement.setString(1, ukrainianTitle);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createProduct(resultSet, connection);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        throw new ProductNotFoundException("No product with this ukrainian title was found");
    }

    @Override
    public Product findByEnglishTitle(Connection connection, String englishTitle) throws SQLException, ProductNotFoundException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE title_eng = ?")) {
            statement.setString(1, englishTitle);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createProduct(resultSet, connection);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        throw new ProductNotFoundException("No product with this english title was found");
    }

    @Override
    public List<Product> findAllOutStock(Connection connection) throws SQLException {
        List<Product> products = new ArrayList<>(20);
        ResultSet resultSet = null;
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(GET_ALL_STOCK_OF_OUT_PRODUCT_SQL);
            while (resultSet.next()) {
                products.add(createProduct(resultSet, connection));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return products;
    }

    @Override
    public List<Product> findAllBetweenDate(Connection connection, Date from, Date to) throws SQLException {
        List<Product> products = new ArrayList<>(20);
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE date_creation >= ? AND date_creation <= ?")) {
            statement.setDate(1, new java.sql.Date(from.getTime()));
            statement.setDate(2, new java.sql.Date(to.getTime()));
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(createProduct(resultSet, connection));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }

        return products;
    }

    @Override
    public List<Product> findAllBetweenPrice(Connection connection, double from, double to) throws SQLException {
        List<Product> products = new ArrayList<>(20);
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products WHERE price >= ? AND price <= ?")) {
            statement.setDouble(1, from);
            statement.setDouble(2, to);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(createProduct(resultSet, connection));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return products;
    }

    @Override
    public List<Product> findAllAddedByUser(Connection connection, User user) throws SQLException, UserNotFoundException {
        List<Product> products = new ArrayList<>(20);
        ResultSet resultSet = null;
        user = userDAO.findByUsername(connection, user.getUsername());
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products JOIN users ON user_id = user_creator_id WHERE user_id = ?")) {
            statement.setLong(1, user.getId());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(createProduct(resultSet, connection));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return products;
    }

    @Override
    public List<Product> findAllAddedByUser(Connection connection, String username) throws SQLException, UserNotFoundException {
        List<Product> products = new ArrayList<>(20);
        ResultSet resultSet = null;
        userDAO.findByUsername(connection, username);
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM products JOIN users ON user_id = user_creator_id WHERE login = ?")) {
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(createProduct(resultSet, connection));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return products;
    }

    @Override
    public void insert(Connection connection, Product product) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO products(title_ukr, title_eng, price, code, quantity, date_creation, user_creator_id) VALUES (?,?,?,?,?,?,?)")) {
            int k = 1;
            preparedStatement.setString(k++, product.getTitle_ukr());
            preparedStatement.setString(k++, product.getTitle_eng());
            preparedStatement.setDouble(k++, product.getPrice());
            preparedStatement.setString(k++, product.getCode());
            preparedStatement.setLong(k++, product.getQuantity());
            preparedStatement.setDate(k++, new java.sql.Date(product.getDateCreation().getTime()));
            preparedStatement.setLong(k++, product.getUserCreator().getId());
            preparedStatement.execute();
        }
    }

    @Override
    public void update(Connection connection, Product product) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE products SET title_ukr = ?, title_eng = ?, price = ?, code = ?, quantity = ?, date_creation = ?, user_creator_id = ? WHERE product_id ?")) {
            int k = 1;
            preparedStatement.setString(k++, product.getTitle_ukr());
            preparedStatement.setString(k++, product.getTitle_eng());
            preparedStatement.setDouble(k++, product.getPrice());
            preparedStatement.setString(k++, product.getCode());
            preparedStatement.setLong(k++, product.getQuantity());
            preparedStatement.setDate(k++, new java.sql.Date(product.getDateCreation().getTime()));
            preparedStatement.setLong(k++, product.getUserCreator().getId());
            preparedStatement.setLong(k++, product.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Connection connection, long product_id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM products WHERE product_id = ?")) {
            preparedStatement.setLong(1, product_id);
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(Connection connection, String code) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM products WHERE code = ?")) {
            preparedStatement.setString(1, code);
            preparedStatement.execute();
        }
    }

    private Product createProduct(ResultSet resultSet, Connection connection) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong(COLUMN_ID_PRODUCT));
        product.setTitle_ukr(resultSet.getString(COLUMN_UKRAINIAN_TITLE_PRODUCT));
        product.setTitle_eng(resultSet.getString(COLUMN_ENGLISH_TITLE_PRODUCT));
        product.setPrice(resultSet.getDouble(COLUMN_PRICE_PRODUCT));
        product.setCode(resultSet.getString(COLUMN_CODE_PRODUCT));
        product.setQuantity(resultSet.getLong(COLUMN_QUANTITY_PRODUCT));
        Date date = resultSet.getDate(COLUMN_DATE_CREATION_PRODUCT);
        product.setDateCreation(date != null ? date : new Date(0));
        try {
            product.setUserCreator(userDAO.findById(connection, resultSet.getLong(COLUMN_USER_CREATOR_ID_PRODUCT)));
        } catch (UserNotFoundException e) {
            product.setUserCreator(null);
        }
        return product;
    }

}
