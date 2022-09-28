package com.epam.cash.register.dao;

import com.epam.cash.register.entity.ItemReceipt;
import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.ReceiptNotFoundException;
import com.epam.cash.register.exception.ReceiptProcessedException;
import com.epam.cash.register.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ReceiptDAO {

    List<Receipt> findAll(Connection connection) throws SQLException;

    Receipt findByCode(Connection connection, String receiptCode) throws SQLException, ReceiptNotFoundException;

    List<Receipt> findByUsername(Connection connection, String creatorName) throws SQLException, UserNotFoundException;

    Receipt findById(Connection connection, long idReceipt) throws SQLException, ReceiptNotFoundException;

    List<Receipt> findAllBetweenDate(Connection connection, Date from, Date to) throws SQLException;

    List<Receipt> findAllCanceled(Connection connection) throws SQLException;

    List<Receipt> findCanceled(Connection connection, String username) throws SQLException, UserNotFoundException;

    List<Receipt> findAllBetweenPrice(Connection connection, double from, double to) throws SQLException;

    Receipt findByProcessedUser(Connection connection, User user) throws SQLException, ReceiptNotFoundException;

    void insert(Connection connection, Receipt receipt) throws SQLException, ReceiptProcessedException;

    void update(Connection connection, Receipt receipt) throws SQLException, ReceiptProcessedException;

    void insert(Connection connection, Receipt... receipts) throws SQLException, ReceiptProcessedException;

    void delete(Connection connection, long id) throws SQLException;

    void delete(Connection connection, long... id) throws SQLException;

    void cancelItemReceipt(Connection connection, ItemReceipt itemReceipt);

    void cancelReceipt(Connection connection, Receipt receipt);
}
