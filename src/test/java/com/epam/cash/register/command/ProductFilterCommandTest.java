package com.epam.cash.register.command;


import com.epam.cash.register.entity.Product;
import com.epam.util.ProductListUtill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductFilterCommandTest {

    @Mock
    private static HttpServletRequest req;

    @Mock
    private static HttpServletResponse resp;


    @BeforeEach
    public void setUp() {

        initRequestParam();

        initRequestAttributes();
    }

    private static void initRequestParam() {
        final Map<String, String[]> map = new HashMap<>();
        map.put("filtering", new String[]{"true"});

        when(req.getParameterMap()).thenReturn(map);

    }

    private static void initRequestAttributes() {
        final Map<String, Object> attributes = new ConcurrentHashMap<>();

        attributes.put("allProducts", ProductListUtill.getListProducts());

        doAnswer((Answer<Void>) invocation -> {
            String key = invocation.getArgument(0, String.class);
            Object value = invocation.getArgument(1, Object.class);
            attributes.put(key, value);
            return null;
        }).when(req).setAttribute(anyString(), Mockito.any());


        doAnswer(invocationOnMock -> {
            String key = invocationOnMock.getArgument(0, String.class);
            return attributes.get(key);
        }).when(req).getAttribute(anyString());
    }

    @Test
    public void testEmptyFilter_ShouldBeOK() throws IOException {
        Command command = new ProductFilterCommand();
        command.execute(req, resp);
        assertEquals(ProductListUtill.getListProducts(), req.getAttribute("allProducts"));
    }

    @Test
    public void testUkrainianTitle_ShouldBeOK() throws IOException {
        Map<String, String[]> map = req.getParameterMap();
        map.put("ukranianTitle", new String[]{"ukrTitle_1"});

        Command command = new ProductFilterCommand();
        command.execute(req, resp);

        assertEquals(ProductListUtill.getListProducts().subList(0, 1), req.getAttribute("allProducts"));
    }

    @Test
    public void testEnglishTitle_ShouldBeOK() throws IOException {
        Map<String, String[]> map = req.getParameterMap();
        map.put("englishTitle", new String[]{"engTitle_1"});

        Command command = new ProductFilterCommand();
        command.execute(req, resp);

        assertEquals(ProductListUtill.getListProducts().subList(0, 1), req.getAttribute("allProducts"));
    }

    @Test
    public void testMinPrice_ShouldBeOK() throws IOException {
        Map<String, String[]> map = req.getParameterMap();
        map.put("minPrice", new String[]{"10000"});

        Command command = new ProductFilterCommand();
        command.execute(req, resp);

        List<Product> products = ProductListUtill.getListProducts();

        List<Product> expected = products.subList(2, 5);

        assertEquals(expected, req.getAttribute("allProducts"));
    }

    @Test
    public void testMaxPrice_ShouldBeOK() throws IOException {
        Map<String, String[]> map = req.getParameterMap();
        map.put("maxPrice", new String[]{"10000"});

        Command command = new ProductFilterCommand();
        command.execute(req, resp);

        List<Product> products = ProductListUtill.getListProducts();

        List<Product> expected = products.subList(0, 3);

        assertEquals(expected, req.getAttribute("allProducts"));
    }

    @Test
    public void testPriceBetween_ShouldBeOK() throws IOException {
        Map<String, String[]> map = req.getParameterMap();
        map.put("minPrice", new String[]{"2000"});
        map.put("maxPrice", new String[]{"10000"});

        Command command = new ProductFilterCommand();
        command.execute(req, resp);

        List<Product> products = ProductListUtill.getListProducts();

        List<Product> expected = List.of(products.get(0), products.get(2));

        assertEquals(expected, req.getAttribute("allProducts"));
    }

    @Test
    public void testQuantityBetweenAndPriceBetween_ShouldBeOK() throws IOException {
        Map<String, String[]> map = req.getParameterMap();
        map.put("maxQuantity", new String[]{"150"});
        map.put("minQuantity", new String[]{"50"});
        map.put("minPrice", new String[]{"2000"});
        map.put("maxPrice", new String[]{"10000"});

        Command command = new ProductFilterCommand();
        command.execute(req, resp);

        List<Product> products = ProductListUtill.getListProducts();

        List<Product> expected = List.of(products.get(0));

        assertEquals(expected, req.getAttribute("allProducts"));
    }
}
