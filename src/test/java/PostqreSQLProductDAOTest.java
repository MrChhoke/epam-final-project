import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import com.epam.cash.register.dao.PostqreSQLProductDAO;
import com.epam.cash.register.dao.ProductDAO;
import com.epam.cash.register.dao.UserDAO;
import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.ProductNotFoundException;
import com.epam.cash.register.exception.UserNotFoundException;
import org.junit.*;


import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostqreSQLProductDAOTest {

    private static final String COLUMN_ID_PRODUCT = "product_id";
    private static final String COLUMN_UKRAINIAN_TITLE_PRODUCT = "title_ukr";
    private static final String COLUMN_ENGLISH_TITLE_PRODUCT = "title_eng";
    private static final String COLUMN_PRICE_PRODUCT = "price";
    private static final String COLUMN_CODE_PRODUCT = "code";
    private static final String COLUMN_QUANTITY_PRODUCT = "quantity";
    private static final String COLUMN_DATE_CREATION_PRODUCT = "date_creation";
    private static final String COLUMN_USER_CREATOR_ID_PRODUCT = "user_creator_id";

    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConn;
    @Mock
    private PreparedStatement mockPreparedStmnt;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private UserDAO mockUserDAO;

    private final Product productExpected;
    private final User userExpected;
    private final List<Product> productsExpected;

    public PostqreSQLProductDAOTest() {
        userExpected = new User(
                1L,
                "Vlad",
                "Password",
                User.Role.ADMIN
        );
        productExpected = new Product(
                1L,
                "123456",
                "Перший продукт",
                "The first product",
                100L,
                200D,
                new Date(0),
                userExpected
        );

        productsExpected = List.of(
                new Product(
                        1L,
                        "12",
                        "Перший продукт",
                        "The first product",
                        100L,
                        200D,
                        new Date(100),
                        userExpected
                ), new Product(
                        2L,
                        "123",
                        "Другий продукт",
                        "The second product",
                        100L,
                        200D,
                        new Date(200),
                        userExpected
                ), new Product(
                        3L,
                        "1234",
                        "Третій продукт",
                        "The third product",
                        100L,
                        200D,
                        new Date(300),
                        userExpected
                ), new Product(
                        4L,
                        "12345",
                        "Четвертий продукт",
                        "4th product",
                        100L,
                        200D,
                        new Date(400),
                        userExpected
                ), new Product(
                        5L,
                        "123456",
                        "П'ятий продукт",
                        "5th product",
                        100L,
                        200D,
                        new Date(500),
                        userExpected
                )
        );
    }

    @Before
    public void setUp() throws SQLException {

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockConn.createStatement()).thenReturn(mockStatement);

        doNothing().when(mockPreparedStmnt).setString(anyInt(), anyString());
        doNothing().when(mockPreparedStmnt).setLong(anyInt(), anyLong());
        doNothing().when(mockPreparedStmnt).setDate(anyInt(), any());

        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);

        when(mockPreparedStmnt.execute()).thenReturn(Boolean.TRUE);

    }

    @Test
    public void testGetProductByID_ShouldBe_OK() throws SQLException, UserNotFoundException, ProductNotFoundException {
        long checkID = productExpected.getId();

        when(mockResultSet.getLong(COLUMN_ID_PRODUCT))
                .thenReturn(productExpected.getId());

        when(mockResultSet.getLong(COLUMN_QUANTITY_PRODUCT))
                .thenReturn(productExpected.getQuantity());

        when(mockResultSet.getString(COLUMN_UKRAINIAN_TITLE_PRODUCT))
                .thenReturn(productExpected.getTitle_ukr());

        when(mockResultSet.getString(COLUMN_ENGLISH_TITLE_PRODUCT))
                .thenReturn(productExpected.getTitle_eng());

        when(mockResultSet.getString(COLUMN_CODE_PRODUCT))
                .thenReturn(productExpected.getCode());

        when(mockResultSet.getDouble(COLUMN_PRICE_PRODUCT))
                .thenReturn(productExpected.getPrice());

        when(mockResultSet.getDate(COLUMN_DATE_CREATION_PRODUCT))
                .thenReturn(new java.sql.Date(productExpected.getDateCreation().getTime()));

        when(mockResultSet.getLong(COLUMN_USER_CREATOR_ID_PRODUCT)).
                thenReturn(productExpected.getUserCreator().getId());

        when(mockUserDAO.findById(mockConn, productExpected.getUserCreator().getId()))
                .thenReturn(productExpected.getUserCreator());

        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false);


        ProductDAO productDAO = new PostqreSQLProductDAO(mockUserDAO);

        Product productFromDB = productDAO.findById(mockConn, checkID);

        compareProduct(productExpected, productFromDB);

    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductByID_ThrowsException_IfProductDoesntExists() throws ProductNotFoundException, SQLException {
        long notExistID = 1903124L;

        when(mockResultSet.next())
                .thenReturn(false);

        ProductDAO productDAO = new PostqreSQLProductDAO();

        productDAO.findById(mockConn, notExistID);
    }

    @Test
    public void testGetAllProducts_ShouldBE_OK() throws SQLException, UserNotFoundException {

        when(mockResultSet.getLong(COLUMN_ID_PRODUCT))
                .thenReturn(productsExpected.get(0).getId())
                .thenReturn(productsExpected.get(1).getId())
                .thenReturn(productsExpected.get(2).getId())
                .thenReturn(productsExpected.get(3).getId())
                .thenReturn(productsExpected.get(4).getId());

        when(mockResultSet.getLong(COLUMN_QUANTITY_PRODUCT))
                .thenReturn(productsExpected.get(0).getQuantity())
                .thenReturn(productsExpected.get(1).getQuantity())
                .thenReturn(productsExpected.get(2).getQuantity())
                .thenReturn(productsExpected.get(3).getQuantity())
                .thenReturn(productsExpected.get(4).getQuantity());

        when(mockResultSet.getString(COLUMN_UKRAINIAN_TITLE_PRODUCT))
                .thenReturn(productsExpected.get(0).getTitle_ukr())
                .thenReturn(productsExpected.get(1).getTitle_ukr())
                .thenReturn(productsExpected.get(2).getTitle_ukr())
                .thenReturn(productsExpected.get(3).getTitle_ukr())
                .thenReturn(productsExpected.get(4).getTitle_ukr());

        when(mockResultSet.getString(COLUMN_ENGLISH_TITLE_PRODUCT))
                .thenReturn(productsExpected.get(0).getTitle_eng())
                .thenReturn(productsExpected.get(1).getTitle_eng())
                .thenReturn(productsExpected.get(2).getTitle_eng())
                .thenReturn(productsExpected.get(3).getTitle_eng())
                .thenReturn(productsExpected.get(4).getTitle_eng());

        when(mockResultSet.getString(COLUMN_CODE_PRODUCT))
                .thenReturn(productsExpected.get(0).getCode())
                .thenReturn(productsExpected.get(1).getCode())
                .thenReturn(productsExpected.get(2).getCode())
                .thenReturn(productsExpected.get(3).getCode())
                .thenReturn(productsExpected.get(4).getCode());

        when(mockResultSet.getDouble(COLUMN_PRICE_PRODUCT))
                .thenReturn(productsExpected.get(0).getPrice())
                .thenReturn(productsExpected.get(1).getPrice())
                .thenReturn(productsExpected.get(2).getPrice())
                .thenReturn(productsExpected.get(3).getPrice())
                .thenReturn(productsExpected.get(4).getPrice());

        when(mockResultSet.getDate(COLUMN_DATE_CREATION_PRODUCT))
                .thenReturn(new java.sql.Date(productsExpected.get(0).getDateCreation().getTime()))
                .thenReturn(new java.sql.Date(productsExpected.get(1).getDateCreation().getTime()))
                .thenReturn(new java.sql.Date(productsExpected.get(2).getDateCreation().getTime()))
                .thenReturn(new java.sql.Date(productsExpected.get(3).getDateCreation().getTime()))
                .thenReturn(new java.sql.Date(productsExpected.get(4).getDateCreation().getTime()));

        when(mockResultSet.getLong(COLUMN_USER_CREATOR_ID_PRODUCT)).
                thenReturn(productExpected.getUserCreator().getId());

        when(mockUserDAO.findById(mockConn, productExpected.getUserCreator().getId()))
                .thenReturn(productExpected.getUserCreator());

        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);


        ProductDAO productDAO = new PostqreSQLProductDAO(mockUserDAO);

        List<Product> productsFromDB = productDAO.findAll(mockConn);

        assertEquals(productsFromDB.size(), productsExpected.size());

        for (int i = 0; i < productsFromDB.size(); i++) {
            compareProduct(productsExpected.get(i), productsFromDB.get(i));
        }
    }

    @Test
    public void testGetAllProductsBetweenTwoDate_ShouldBE_OK() throws SQLException, UserNotFoundException {
        List<Product> productsExpected = this.productsExpected.stream()
                .filter(product -> product.getDateCreation().getTime() >= 200
                        && product.getDateCreation().getTime() <= 400)
                .collect(Collectors.toList());

        when(mockResultSet.getLong(COLUMN_ID_PRODUCT))
                .thenReturn(productsExpected.get(0).getId())
                .thenReturn(productsExpected.get(1).getId())
                .thenReturn(productsExpected.get(2).getId());

        when(mockResultSet.getLong(COLUMN_QUANTITY_PRODUCT))
                .thenReturn(productsExpected.get(0).getQuantity())
                .thenReturn(productsExpected.get(1).getQuantity())
                .thenReturn(productsExpected.get(2).getQuantity());

        when(mockResultSet.getString(COLUMN_UKRAINIAN_TITLE_PRODUCT))
                .thenReturn(productsExpected.get(0).getTitle_ukr())
                .thenReturn(productsExpected.get(1).getTitle_ukr())
                .thenReturn(productsExpected.get(2).getTitle_ukr());

        when(mockResultSet.getString(COLUMN_ENGLISH_TITLE_PRODUCT))
                .thenReturn(productsExpected.get(0).getTitle_eng())
                .thenReturn(productsExpected.get(1).getTitle_eng())
                .thenReturn(productsExpected.get(2).getTitle_eng());

        when(mockResultSet.getString(COLUMN_CODE_PRODUCT))
                .thenReturn(productsExpected.get(0).getCode())
                .thenReturn(productsExpected.get(1).getCode())
                .thenReturn(productsExpected.get(2).getCode());

        when(mockResultSet.getDouble(COLUMN_PRICE_PRODUCT))
                .thenReturn(productsExpected.get(0).getPrice())
                .thenReturn(productsExpected.get(1).getPrice())
                .thenReturn(productsExpected.get(2).getPrice());

        when(mockResultSet.getDate(COLUMN_DATE_CREATION_PRODUCT))
                .thenReturn(new java.sql.Date(productsExpected.get(0).getDateCreation().getTime()))
                .thenReturn(new java.sql.Date(productsExpected.get(1).getDateCreation().getTime()))
                .thenReturn(new java.sql.Date(productsExpected.get(2).getDateCreation().getTime()));

        when(mockResultSet.getLong(COLUMN_USER_CREATOR_ID_PRODUCT)).
                thenReturn(productExpected.getUserCreator().getId());

        when(mockUserDAO.findById(mockConn, productExpected.getUserCreator().getId()))
                .thenReturn(productExpected.getUserCreator());

        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);


        ProductDAO productDAO = new PostqreSQLProductDAO(mockUserDAO);

        List<Product> productsFromDB = productDAO.findAllBetweenDate(mockConn, new Date(200), new Date(400));

        assertEquals(productsFromDB.size(), productsExpected.size());

        for (int i = 0; i < productsFromDB.size(); i++) {
            compareProduct(productsExpected.get(i), productsFromDB.get(i));
        }
    }

    @Test
    public void testEnglishTitle_ShouldBe_OK() throws SQLException, UserNotFoundException, ProductNotFoundException {
        String productEnglishTitle = "The first product";

        Product productExpected = productsExpected.stream()
                .filter(item -> item.getTitle_eng().equals(productEnglishTitle))
                .findFirst()
                .get();

        when(mockResultSet.getLong(COLUMN_ID_PRODUCT))
                .thenReturn(productExpected.getId());

        when(mockResultSet.getLong(COLUMN_QUANTITY_PRODUCT))
                .thenReturn(productExpected.getQuantity());

        when(mockResultSet.getString(COLUMN_UKRAINIAN_TITLE_PRODUCT))
                .thenReturn(productExpected.getTitle_ukr());

        when(mockResultSet.getString(COLUMN_ENGLISH_TITLE_PRODUCT))
                .thenReturn(productExpected.getTitle_eng());

        when(mockResultSet.getString(COLUMN_CODE_PRODUCT))
                .thenReturn(productExpected.getCode());

        when(mockResultSet.getDouble(COLUMN_PRICE_PRODUCT))
                .thenReturn(productExpected.getPrice());

        when(mockResultSet.getDate(COLUMN_DATE_CREATION_PRODUCT))
                .thenReturn(new java.sql.Date(productExpected.getDateCreation().getTime()));

        when(mockResultSet.getLong(COLUMN_USER_CREATOR_ID_PRODUCT)).
                thenReturn(productExpected.getUserCreator().getId());

        when(mockUserDAO.findById(mockConn, productExpected.getUserCreator().getId()))
                .thenReturn(productExpected.getUserCreator());

        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false);


        ProductDAO productDAO = new PostqreSQLProductDAO(mockUserDAO);

        Product productFromDB = productDAO.findByEnglishTitle(mockConn, productEnglishTitle);

        compareProduct(productExpected, productFromDB);
    }

    @Test
    public void testUkrainianTitle_ShouldBe_OK() throws SQLException, UserNotFoundException, ProductNotFoundException {
        String productUkrainianTitle = "Четвертий продукт";

        Product productExpected = productsExpected.stream()
                .filter(item -> item.getTitle_ukr().equals(productUkrainianTitle))
                .findFirst()
                .get();

        when(mockResultSet.getLong(COLUMN_ID_PRODUCT))
                .thenReturn(productExpected.getId());

        when(mockResultSet.getLong(COLUMN_QUANTITY_PRODUCT))
                .thenReturn(productExpected.getQuantity());

        when(mockResultSet.getString(COLUMN_UKRAINIAN_TITLE_PRODUCT))
                .thenReturn(productExpected.getTitle_ukr());

        when(mockResultSet.getString(COLUMN_ENGLISH_TITLE_PRODUCT))
                .thenReturn(productExpected.getTitle_eng());

        when(mockResultSet.getString(COLUMN_CODE_PRODUCT))
                .thenReturn(productExpected.getCode());

        when(mockResultSet.getDouble(COLUMN_PRICE_PRODUCT))
                .thenReturn(productExpected.getPrice());

        when(mockResultSet.getDate(COLUMN_DATE_CREATION_PRODUCT))
                .thenReturn(new java.sql.Date(productExpected.getDateCreation().getTime()));

        when(mockResultSet.getLong(COLUMN_USER_CREATOR_ID_PRODUCT)).
                thenReturn(productExpected.getUserCreator().getId());

        when(mockUserDAO.findById(mockConn, productExpected.getUserCreator().getId()))
                .thenReturn(productExpected.getUserCreator());

        when(mockResultSet.next())
                .thenReturn(true)
                .thenReturn(false);

        ProductDAO productDAO = new PostqreSQLProductDAO(mockUserDAO);

        Product productFromDB = productDAO.findByUkrainianTitle(mockConn, productUkrainianTitle);

        compareProduct(productExpected, productFromDB);
    }

    @Test
    public void testInsertProduct() throws SQLException, ProductNotFoundException, UserNotFoundException {
        ProductDAO productDAO = new PostqreSQLProductDAO(mockUserDAO);

        when(mockResultSet.next())
                .thenReturn(false)
                .thenReturn(true);

        assertThrows(ProductNotFoundException.class,
                () -> productDAO.findById(mockConn, productsExpected.get(0).getId()));

        productDAO.insert(mockConn, productsExpected.get(0));

        when(mockResultSet.getLong(COLUMN_ID_PRODUCT))
                .thenReturn(productsExpected.get(0).getId());

        when(mockResultSet.getLong(COLUMN_QUANTITY_PRODUCT))
                .thenReturn(productsExpected.get(0).getQuantity());

        when(mockResultSet.getString(COLUMN_UKRAINIAN_TITLE_PRODUCT))
                .thenReturn(productsExpected.get(0).getTitle_ukr());

        when(mockResultSet.getString(COLUMN_ENGLISH_TITLE_PRODUCT))
                .thenReturn(productsExpected.get(0).getTitle_eng());

        when(mockResultSet.getString(COLUMN_CODE_PRODUCT))
                .thenReturn(productsExpected.get(0).getCode());

        when(mockResultSet.getDouble(COLUMN_PRICE_PRODUCT))
                .thenReturn(productsExpected.get(0).getPrice());

        when(mockResultSet.getDate(COLUMN_DATE_CREATION_PRODUCT))
                .thenReturn(new java.sql.Date(productsExpected.get(0).getDateCreation().getTime()));

        when(mockResultSet.getLong(COLUMN_USER_CREATOR_ID_PRODUCT)).
                thenReturn(productsExpected.get(0).getUserCreator().getId());

        when(mockUserDAO.findById(mockConn, productExpected.getUserCreator().getId()))
                .thenReturn(productsExpected.get(0).getUserCreator());

        Product productFromDB = productDAO.findById(mockConn, productsExpected.get(0).getId());

        compareProduct(productsExpected.get(0), productFromDB);
    }


    private void compareProduct(Product productExpected, Product actual) {
        assertNotSame(productExpected, actual);
        assertEquals(productExpected.getId(), actual.getId());
        assertEquals(productExpected.getTitle_eng(), actual.getTitle_eng());
        assertEquals(productExpected.getTitle_ukr(), actual.getTitle_ukr());
        assertEquals(productExpected.getCode(), actual.getCode());
        assertEquals(productExpected.getPrice(), actual.getPrice(), 0.0001);
        assertEquals(productExpected.getDateCreation(), actual.getDateCreation());
        assertEquals(productExpected.getUserCreator().getId(), actual.getUserCreator().getId());
    }
}