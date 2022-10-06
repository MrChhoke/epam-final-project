package com.epam.cash.register.service;

import com.epam.cash.register.dao.PostqreSQLReceiptDAO;
import com.epam.cash.register.dao.ReceiptDAO;
import com.epam.cash.register.entity.ItemReceipt;
import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.ReceiptProcessedException;
import com.epam.cash.register.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ReceiptServiceImpl implements ReceiptService {

    private static final Logger log = LogManager.getLogger(ReceiptServiceImpl.class);

    private final ReceiptDAO receiptDAO;

    public ReceiptServiceImpl() {
        receiptDAO = new PostqreSQLReceiptDAO();
    }

    public ReceiptServiceImpl(ReceiptDAO receiptDAO) {
        this.receiptDAO = receiptDAO;
    }

    @Override
    public List<Receipt> findAll() throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findAll(connection);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public Receipt findByCode(String receiptCode) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findByCode(connection, receiptCode);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Receipt> findByUsername(String creatorName) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findByUsername(connection, creatorName);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public Receipt findById(long idReceipt) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findById(connection, idReceipt);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Receipt> findAllBetweenDate(Date from, Date to) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findAllBetweenDate(connection, from, to);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Receipt> findAllCanceled() throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findAllCanceled(connection);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Receipt> findCanceled(String username) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findCanceled(connection, username);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public List<Receipt> findAllBetweenPrice(double from, double to) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findAllBetweenPrice(connection, from, to);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public Receipt findByProcessedUser(User user) throws IOException {
        try (Connection connection = DBUtil.getConnection()) {
            return receiptDAO.findByProcessedUser(connection, user);
        } catch (SQLException exp) {
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        }
    }

    @Override
    public void insert(Receipt receipt) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);

            receipt.getItems().removeIf(itemReceipt -> itemReceipt.getQuantity() <= 0L);

            log.trace(receipt.getItems().size());

            receiptDAO.insert(connection, receipt);
            connection.commit();
        } catch (SQLException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(Receipt receipt) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);

            receipt.getItems().removeIf(itemReceipt -> itemReceipt.getQuantity() <= 0L);

            receiptDAO.update(connection, receipt);
            connection.commit();
        } catch (SQLException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void insert(Receipt... receipts) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            receiptDAO.insert(connection, receipts);
            connection.commit();
        } catch (SQLException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void delete(long id) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            receiptDAO.delete(connection, id);
            connection.commit();
        } catch (SQLException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void delete(long... id) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            receiptDAO.delete(connection, id);
            connection.commit();
        } catch (SQLException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void cancelItemReceipt(ItemReceipt itemReceipt) throws IOException {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            receiptDAO.cancelItemReceipt(connection, itemReceipt);
            connection.commit();
        } catch (SQLException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void cancelReceipt(Receipt receipt, User canceler) throws IOException {
        Connection connection = null;
        try {
            receipt.setUserCanceler(canceler);
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            receiptDAO.cancelReceipt(connection, receipt);
            connection.commit();
        } catch (SQLException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void acceptReceipt(Receipt receipt) throws IOException {
        receipt.setDone(true);
        update(receipt);
    }

    @Override
    public void cancelReceipt(String codeReceitp, User userCanceler) throws IOException {
        Connection connection = null;
        try {
            Receipt canceledReceipt = findByCode(codeReceitp);
            canceledReceipt.setUserCanceler(userCanceler);
            canceledReceipt.setCanceled(true);
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            receiptDAO.cancelReceipt(connection, canceledReceipt);
            connection.commit();
        } catch (SQLException exp) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.error("Ooops, something is wrong", exp);
            throw new IOException("Ooops, something is wrong", exp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
