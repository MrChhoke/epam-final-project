package com.epam.cash.register.dao;

import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.exception.ReceiptNotFoundException;
import com.epam.cash.register.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ReceiptDAO {

    List<Receipt> findAll(Connection connection) throws SQLException;

    List<Receipt> findByUsername(Connection connection, String creatorName) throws SQLException, UserNotFoundException;

    Receipt findById(Connection connection, long id) throws SQLException, ReceiptNotFoundException;

    List<Receipt> findAllBetweenDate(Connection connection, Date from, Date to) throws SQLException;

    List<Receipt> findAllCanceled(Connection connection) throws SQLException;

    List<Receipt> findCanceled(Connection connection, String username) throws SQLException, UserNotFoundException;

    List<Receipt> findAllBetweenPrice(Connection connection, double from, double to) throws SQLException;

    void insert(Connection connection, Receipt receipt) throws SQLException;

    void update(Connection connection, Receipt receipt) throws SQLException;

    void insert(Connection connection, Receipt... receipts) throws SQLException;

    void delete(Connection connection, long id) throws SQLException;

    void delete(Connection connection, long... id) throws SQLException;
}
