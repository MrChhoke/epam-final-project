package com.epam.cash.register.service;

import com.epam.cash.register.entity.ItemReceipt;
import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.ReceiptNotFoundException;
import com.epam.cash.register.exception.ReceiptProcessedException;
import com.epam.cash.register.exception.UserNotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ReceiptService {

    List<Receipt> findAll() throws IOException;

    Receipt findByCode(String receiptCode) throws IOException;

    List<Receipt> findByUsername(String creatorName) throws IOException;

    Receipt findById(long idReceipt) throws SQLException, IOException;

    List<Receipt> findAllBetweenDate(Date from, Date to) throws IOException;

    List<Receipt> findAllCanceled() throws IOException;

    List<Receipt> findCanceled(String username) throws IOException;

    List<Receipt> findAllBetweenPrice(double from, double to) throws IOException;

    Receipt findByProcessedUser(User user) throws IOException;

    void insert(Receipt receipt) throws IOException;

    void update(Receipt receipt) throws IOException;

    void insert(Receipt... receipts) throws IOException;

    void delete(long id) throws IOException;

    void delete(long... id) throws IOException;

    void cancelItemReceipt(ItemReceipt itemReceipt) throws IOException;

    void cancelReceipt(Receipt receipt, User canceler) throws IOException;

    void acceptReceipt(Receipt receipt) throws IOException;

    void cancelReceipt(String codeReceitp, User candeler) throws IOException;
}
