package com.epam.cash.register.dao;

import com.epam.cash.register.entity.ItemReceipt;
import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.exception.ReceiptNotFoundException;
import com.epam.cash.register.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostqreSQLReceiptDAO implements ReceiptDAO {

    private static final String RECEIPT_ID = "receipt_id";
    private static final String RECEIPT_PRICE = "total_price";
    private static final String RECEIPT_USER_CREATOR_ID = "user_creator_id";
    private static final String RECEIPT_DATE_CREATION = "date_creation";
    private static final String RECEIPT_CANCELED = "isCanceled";
    private static final String RECEIPT_USER_CANCELER_ID = "user_canceler_id";

    private static final String ITEM_RECEIPT_ID = "item_receipt_id";
    private static final String ITEM_RECEIPT_PRODUCT_ID = "product_id";
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
             PreparedStatement pstmtProductItemsReceipt = connection.prepareStatement("SELECT p.product_id FROM items_receipt ir JOIN products p ON ir.product_id = p.product_id");
             PreparedStatement pstmtItemsReceipt = connection.prepareStatement("SELECT  * FROM receipts_products rp JOIN items_receipt ir on rp.item_receipt_id = ir.item_receipt_id");
             PreparedStatement pstmtItemsReceiptsID = connection.prepareStatement("SELECT * FROM receipts_products")) {

            rs = pstmtReceipts.executeQuery();
            receipts = getReceipts(rs,connection);
            rs.close();

            rs = pstmtProductItemsReceipt.executeQuery();
            List<Product> products = getAllProducts(rs, connection);
            rs.close();

            rs = pstmtItemsReceipt.executeQuery();
            List<ItemReceipt> itemReceipts = getItemsReceipts(rs,connection,products);
            rs.close();

            rs = pstmtItemsReceiptsID.executeQuery();
            receipts = mapItemReceiptToReceipts(rs,itemReceipts,receipts);
            rs.close();
            return receipts;
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }
    }

    @Override
    public List<Receipt> findByUsername(Connection connection, String creatorName) throws SQLException, UserNotFoundException {
        return null;
    }

    @Override
    public Receipt findById(Connection connection, long id) throws SQLException, ReceiptNotFoundException {
        return null;
    }

    @Override
    public List<Receipt> findAllBetweenDate(Connection connection, Date from, Date to) throws SQLException {
        return null;
    }

    @Override
    public List<Receipt> findAllCanceled(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Receipt> findCanceled(Connection connection, String username) throws SQLException, UserNotFoundException {
        return null;
    }

    @Override
    public List<Receipt> findAllBetweenPrice(Connection connection, double from, double to) throws SQLException {
        return null;
    }

    @Override
    public void insert(Connection connection, Receipt receipt) throws SQLException {

    }

    @Override
    public void update(Connection connection, Receipt receipt) throws SQLException {

    }

    @Override
    public void insert(Connection connection, Receipt... receipts) throws SQLException {

    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {

    }

    @Override
    public void delete(Connection connection, long... id) throws SQLException {

    }

    private List<Receipt> getReceipts(ResultSet rs, Connection connection) throws SQLException {
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

    private List<Product> getAllProducts(ResultSet rs, Connection connection) throws SQLException {
        List<Long> idProducts = new ArrayList<>(100);
        while (rs.next()) {
            idProducts.add(rs.getLong(1));
        }
        return productDAO.findById(connection, idProducts);
    }

    private List<ItemReceipt> getItemsReceipts(ResultSet rs, Connection connection, List<Product> products) throws SQLException{
        List<ItemReceipt> itemsReceipts = new ArrayList<>(100);
        ItemReceipt itemReceipt;
        while(rs.next()){
            itemReceipt = new ItemReceipt();
            itemReceipt.setId(rs.getLong(ITEM_RECEIPT_ID));
            long productID = rs.getLong(ITEM_RECEIPT_PRODUCT_ID);
            itemReceipt.setProduct(products.stream()
                    .filter(product -> product.getId() == productID)
                    .findFirst()
                    .orElse(null)
            );
            itemReceipt.setCanceled(rs.getBoolean(ITEM_RECEIPT_CANCELED));
            try {
                itemReceipt.setUserCanceler(userDAO.findById(connection,rs.getLong(ITEM_RECEIPT_CANCELER_USER_ID)));

            }catch (UserNotFoundException userNotFoundException){
                userNotFoundException.printStackTrace();
            }
            itemsReceipts.add(itemReceipt);
        }
        return itemsReceipts;
    }

    private static final String RECEIPTS_PRODUCTS_RECEIPT_ID = "receipt_id";
    private static final String RECEIPTS_PRODUCTS_ITEM_RECEIPT_ID = "item_receipt_id";

    private List<Receipt> mapItemReceiptToReceipts(ResultSet rs, List<ItemReceipt> itemReceipts, List<Receipt> receipts) throws SQLException{
        Receipt emptyReceipt = new Receipt();
        while(rs.next()){
            long receipt_id = rs.getLong(RECEIPTS_PRODUCTS_RECEIPT_ID);
            long item_receipt_id = rs.getLong(RECEIPTS_PRODUCTS_ITEM_RECEIPT_ID);
            receipts.stream()
                    .filter(receipt -> receipt.getId() == receipt_id)
                    .findFirst()
                    .orElse(emptyReceipt)
                    .addItemReceipt(
                            itemReceipts.stream()
                                    .filter(itemReceipt -> itemReceipt.getId() == item_receipt_id)
                                    .findFirst()
                                    .orElse(null)
                    );
        }
        return receipts;
    }
}
