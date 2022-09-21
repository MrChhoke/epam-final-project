package com.epam.cash.register.dao;

import com.epam.cash.register.entity.ItemReceipt;
import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.exception.ReceiptNotFoundException;
import com.epam.cash.register.exception.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PostqreSQLReceiptDAO implements ReceiptDAO {

    private static final String RECEIPT_ID = "receipt_id";
    private static final String RECEIPT_PRICE = "total_price";
    private static final String RECEIPT_USER_CREATOR_ID = "user_creator_id";
    private static final String RECEIPT_DATE_CREATION = "date_creation";
    private static final String RECEIPT_CANCELED = "isCanceled";
    private static final String RECEIPT_USER_CANCELER_ID = "user_canceler_id";

    private static final String ITEM_RECEIPT_ID = "item_receipt_id";
    private static final String ITEM_RECEIPT_PRODUCT_ID = "product_id";
    private static final String ITEM_RECEIPT_FOREING_KEY_RECEIPT_ID = "receipt_id";
    private static final String ITEM_RECEIPT_CANCELED = "isCanceled";
    private static final String ITEM_RECEIPT_CANCELER_USER_ID = "canceler_id";

    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public PostqreSQLReceiptDAO() {
        productDAO = new PostqreSQLProductDAO();
        userDAO = new PostqreSQLUserDAO();
    }

    @Override
    public List<Receipt> findAll(Connection connection) throws SQLException {
        List<Receipt> receipts;
        ResultSet rs = null;
        try (PreparedStatement pstmtReceipts = connection.prepareStatement("SELECT * FROM receipts");
             PreparedStatement pstmtIDProducts = connection.prepareStatement("SELECT product_id FROM products WHERE product_id IN (SELECT items_receipt.product_id FROM items_receipt)");
             PreparedStatement pstmtItemsReceipt = connection.prepareStatement("SELECT * FROM items_receipt")) {

            rs = pstmtReceipts.executeQuery();
            receipts = getReceiptsWithoutItemsReceipt(rs, connection);
            rs.close();

            rs = pstmtIDProducts.executeQuery();
            List<Product> products = getAllProductsByIDs(rs, connection);
            rs.close();

            rs = pstmtItemsReceipt.executeQuery();
            List<ItemReceipt> itemReceipts = getItemsReceipts(rs, connection, products);
            rs.close();

            mapItemReceiptToReceipts(itemReceipts, receipts);
            return receipts;
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }

    @Override
    public List<Receipt> findByUsername(Connection connection, String creatorName) throws SQLException, UserNotFoundException {
        List<Receipt> receipts;
        ResultSet rs = null;
        long idUser = userDAO.findByUsername(connection, creatorName).getId();

        try (PreparedStatement pstmtReceipts = connection.prepareStatement("SELECT * FROM receipts WHERE user_creator_id = ?");
             PreparedStatement pstmtIDProducts = connection.prepareStatement("SELECT product_id FROM products WHERE product_id IN (SELECT ir.product_id FROM items_receipt ir JOIN receipts r on ir.receipt_id = r.receipt_id WHERE user_creator_id = ?)");
             PreparedStatement pstmtItemsReceipt = connection.prepareStatement("SELECT ir.* FROM items_receipt ir JOIN receipts r on ir.receipt_id = r.receipt_id WHERE user_creator_id = ?")) {

            pstmtReceipts.setLong(1, idUser);
            rs = pstmtReceipts.executeQuery();
            receipts = getReceiptsWithoutItemsReceipt(rs, connection);
            rs.close();

            pstmtIDProducts.setLong(1, idUser);
            rs = pstmtIDProducts.executeQuery();
            List<Product> products = getAllProductsByIDs(rs, connection);
            rs.close();

            pstmtItemsReceipt.setLong(1, idUser);
            rs = pstmtItemsReceipt.executeQuery();
            List<ItemReceipt> itemReceipts = getItemsReceipts(rs, connection, products);
            rs.close();

            mapItemReceiptToReceipts(itemReceipts, receipts);
            return receipts;
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }

    @Override
    public Receipt findById(Connection connection, long idReceipt) throws SQLException, ReceiptNotFoundException {
        List<Receipt> receipts;
        ResultSet rs = null;

        try (PreparedStatement pstmtReceipts = connection.prepareStatement("SELECT * FROM receipts WHERE receipt_id = ?");
             PreparedStatement pstmtIDProducts = connection.prepareStatement("SELECT product_id FROM products WHERE product_id IN (SELECT ir.product_id FROM items_receipt ir WHERE receipt_id = ?)");
             PreparedStatement pstmtItemsReceipt = connection.prepareStatement("SELECT ir.* FROM items_receipt ir WHERE receipt_id = ?")) {

            pstmtReceipts.setLong(1, idReceipt);
            rs = pstmtReceipts.executeQuery();
            receipts = getReceiptsWithoutItemsReceipt(rs, connection);
            rs.close();

            if (receipts.size() == 0) {
                throw new ReceiptNotFoundException("No one receipt was found");
            }

            pstmtIDProducts.setLong(1, idReceipt);
            rs = pstmtIDProducts.executeQuery();
            List<Product> products = getAllProductsByIDs(rs, connection);
            rs.close();

            pstmtItemsReceipt.setLong(1, idReceipt);
            rs = pstmtItemsReceipt.executeQuery();
            List<ItemReceipt> itemReceipts = getItemsReceipts(rs, connection, products);
            rs.close();

            mapItemReceiptToReceipts(itemReceipts, receipts);
            return receipts.get(0);
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }

    @Override
    public List<Receipt> findAllBetweenDate(Connection connection, Date from, Date to) throws SQLException {
        List<Receipt> receipts;
        ResultSet rs = null;

        try (PreparedStatement pstmtReceipts = connection.prepareStatement("SELECT * FROM receipts WHERE date_creation >= ? AND date_creation <= ?");
             PreparedStatement pstmtIDProducts = connection.prepareStatement("SELECT product_id FROM products WHERE product_id IN (SELECT ir.product_id FROM items_receipt ir JOIN receipts r on ir.receipt_id = r.receipt_id WHERE date_creation >= ? AND date_creation <= ?)");
             PreparedStatement pstmtItemsReceipt = connection.prepareStatement("SELECT ir.* FROM items_receipt ir JOIN receipts r on ir.receipt_id = r.receipt_id WHERE date_creation >= ? AND date_creation <= ?")) {

            pstmtReceipts.setDate(1, new java.sql.Date(from.getTime()));
            pstmtReceipts.setDate(2, new java.sql.Date(to.getTime()));
            rs = pstmtReceipts.executeQuery();
            receipts = getReceiptsWithoutItemsReceipt(rs, connection);
            rs.close();


            pstmtIDProducts.setDate(1, new java.sql.Date(from.getTime()));
            pstmtIDProducts.setDate(2, new java.sql.Date(to.getTime()));

            rs = pstmtIDProducts.executeQuery();
            List<Product> products = getAllProductsByIDs(rs, connection);
            rs.close();

            pstmtItemsReceipt.setDate(1, new java.sql.Date(from.getTime()));
            pstmtItemsReceipt.setDate(2, new java.sql.Date(to.getTime()));
            rs = pstmtItemsReceipt.executeQuery();
            List<ItemReceipt> itemReceipts = getItemsReceipts(rs, connection, products);
            rs.close();

            mapItemReceiptToReceipts(itemReceipts, receipts);
            return receipts;
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }

    @Override
    public List<Receipt> findAllCanceled(Connection connection) throws SQLException {
        List<Receipt> receipts;
        ResultSet rs = null;

        try (PreparedStatement pstmtReceipts = connection.prepareStatement("SELECT * FROM receipts WHERE iscanceled = ?");
             PreparedStatement pstmtIDProducts = connection.prepareStatement("SELECT product_id FROM products WHERE product_id IN (SELECT ir.product_id FROM items_receipt ir JOIN receipts r on ir.receipt_id = r.receipt_id WHERE r.iscanceled = ?)");
             PreparedStatement pstmtItemsReceipt = connection.prepareStatement("SELECT ir.* FROM items_receipt ir JOIN receipts r on ir.receipt_id = r.receipt_id WHERE r.iscanceled = ?")) {

            pstmtReceipts.setBoolean(1, true);
            rs = pstmtReceipts.executeQuery();
            receipts = getReceiptsWithoutItemsReceipt(rs, connection);
            rs.close();


            pstmtIDProducts.setBoolean(1, true);
            rs = pstmtIDProducts.executeQuery();
            List<Product> products = getAllProductsByIDs(rs, connection);
            rs.close();

            pstmtItemsReceipt.setBoolean(1, true);
            rs = pstmtItemsReceipt.executeQuery();
            List<ItemReceipt> itemReceipts = getItemsReceipts(rs, connection, products);
            rs.close();

            mapItemReceiptToReceipts(itemReceipts, receipts);
            return receipts;
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }

    @Override
    public List<Receipt> findCanceled(Connection connection, String username) throws SQLException, UserNotFoundException {
        List<Receipt> receipts;
        ResultSet rs = null;
        long idUserCanceler = userDAO.findByUsername(connection, username).getId();

        try (PreparedStatement pstmtReceipts = connection.prepareStatement("SELECT * FROM receipts WHERE user_canceler_id = ?");
             PreparedStatement pstmtIDProducts = connection.prepareStatement("SELECT product_id FROM products WHERE product_id IN (SELECT ir.product_id FROM items_receipt ir JOIN receipts r on ir.receipt_id = r.receipt_id WHERE r.user_canceler_id = ?)");
             PreparedStatement pstmtItemsReceipt = connection.prepareStatement("SELECT ir.* FROM items_receipt ir JOIN receipts r on ir.receipt_id = r.receipt_id WHERE r.user_canceler_id = ?")) {

            pstmtReceipts.setLong(1, idUserCanceler);
            rs = pstmtReceipts.executeQuery();
            receipts = getReceiptsWithoutItemsReceipt(rs, connection);
            rs.close();


            pstmtIDProducts.setLong(1, idUserCanceler);
            rs = pstmtIDProducts.executeQuery();
            List<Product> products = getAllProductsByIDs(rs, connection);
            rs.close();

            pstmtItemsReceipt.setLong(1, idUserCanceler);
            rs = pstmtItemsReceipt.executeQuery();
            List<ItemReceipt> itemReceipts = getItemsReceipts(rs, connection, products);
            rs.close();

            mapItemReceiptToReceipts(itemReceipts, receipts);
            return receipts;
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }

    @Override
    public List<Receipt> findAllBetweenPrice(Connection connection, double from, double to) throws SQLException {
        return null;
    }

    @Override
    public void insert(Connection connection, Receipt receipt) throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO receipts(total_price, user_creator_id, date_creation, iscanceled) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDouble(1, receipt.getTotalPrice());
            preparedStatement.setLong(2, receipt.getUserCreator().getId());
            preparedStatement.setDate(3, new java.sql.Date(receipt.getDateCreation().getTime()));
            preparedStatement.setBoolean(4, receipt.isCanceled());
            preparedStatement.executeUpdate();
            rs = preparedStatement.getGeneratedKeys();
            long idReceipt = 0;
            if (rs.next()) {
                idReceipt = rs.getInt(1);
                receipt.setId(idReceipt);
            }
            rs.close();
            for (ItemReceipt itemReceipt : receipt.getItems()) {
                long idItemReceipt = insertItemReceipt(connection, itemReceipt, idReceipt);
                itemReceipt.setId(idItemReceipt);
            }
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }

    @Override
    public void update(Connection connection, Receipt receipt) throws SQLException {
        delete(connection,receipt.getId());
        insert(connection, receipt);
    }

    @Override
    public void insert(Connection connection, Receipt... receipts) throws SQLException {
        for(Receipt receipt : receipts){
            insert(connection,receipt);
        }
    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {
        try(PreparedStatement pstmt = connection.prepareStatement("DELETE FROM receipts WHERE receipt_id = ?")){
            pstmt.setLong(1,id);
            pstmt.execute();
        }
    }

    @Override
    public void delete(Connection connection, long... id) throws SQLException {
        try(PreparedStatement pstmt = connection.prepareStatement("DELETE FROM receipts WHERE receipt_id IN (?)")){
            pstmt.setString(1, Arrays.stream(id).mapToObj(Long::toString).collect(Collectors.joining(",")));
            pstmt.execute();
        }
    }

    private List<Receipt> getReceiptsWithoutItemsReceipt(ResultSet rs, Connection connection) throws SQLException {
        List<Receipt> receipts = new ArrayList<>(50);
        Receipt receipt;
        while (rs.next()) {
            receipt = new Receipt();
            receipt.setId(rs.getLong(RECEIPT_ID));
            receipt.setTotalPrice(rs.getDouble(RECEIPT_PRICE));
            receipt.setDateCreation(rs.getDate(RECEIPT_DATE_CREATION));
            receipt.setCanceled(rs.getBoolean(RECEIPT_CANCELED));
            try {
                receipt.setUserCreator(userDAO.findById(connection, rs.getLong(RECEIPT_USER_CREATOR_ID)));
                receipt.setUserCanceler(userDAO.findById(connection, rs.getLong(RECEIPT_USER_CANCELER_ID)));
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
            receipts.add(receipt);
        }
        return receipts;
    }

    private List<Product> getAllProductsByIDs(ResultSet rs, Connection connection) throws SQLException {
        List<Long> idProducts = new ArrayList<>(100);
        while (rs.next()) {
            idProducts.add(rs.getLong(1));
        }
        return productDAO.findById(connection, idProducts);
    }

    private List<ItemReceipt> getItemsReceipts(ResultSet rs, Connection connection, List<Product> products) throws SQLException {
        List<ItemReceipt> itemsReceipts = new ArrayList<>(100);
        ItemReceipt itemReceipt;
        while (rs.next()) {
            itemReceipt = new ItemReceipt();
            itemReceipt.setId(rs.getLong(ITEM_RECEIPT_ID));
            long productID = rs.getLong(ITEM_RECEIPT_PRODUCT_ID);
            itemReceipt.setProduct(products.stream()
                    .filter(product -> product.getId() == productID)
                    .findFirst()
                    .orElse(null)
            );
            itemReceipt.setIdReceipt(rs.getLong(ITEM_RECEIPT_FOREING_KEY_RECEIPT_ID));
            itemReceipt.setCanceled(rs.getBoolean(ITEM_RECEIPT_CANCELED));
            try {
                itemReceipt.setUserCanceler(userDAO.findById(connection, rs.getLong(ITEM_RECEIPT_CANCELER_USER_ID)));

            } catch (UserNotFoundException userNotFoundException) {
                userNotFoundException.printStackTrace();
            }
            itemsReceipts.add(itemReceipt);
        }
        return itemsReceipts;
    }

    private void mapItemReceiptToReceipts(List<ItemReceipt> itemReceipts, List<Receipt> receipts) {
        Receipt emptyReceipt = new Receipt();
        itemReceipts.stream()
                .forEach(itemReceipt -> receipts.stream()
                        .filter(receipt -> receipt.getId() == itemReceipt.getIdReceipt())
                        .findFirst()
                        .orElse(emptyReceipt)
                        .addItemReceipt(itemReceipt)
                );
    }

    private long insertItemReceipt(Connection con, ItemReceipt itemReceipt, long receiptID) throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO items_receipt(product_id, receipt_id, iscanceled) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            int k = 1;
            preparedStatement.setLong(k++, itemReceipt.getProduct().getId());
            preparedStatement.setLong(k++, receiptID);
            preparedStatement.setBoolean(k++, itemReceipt.isCanceled());
            preparedStatement.executeUpdate();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }
}
