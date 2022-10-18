package com.epam.cash.register.command;

import com.epam.cash.register.entity.Receipt;
import com.epam.util.ReceiptListUtil;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptFilterCommandTest {

    @Mock
    private static HttpServletRequest req;

    @Mock
    private static HttpServletResponse resp;

    @Mock
    private static Logger logger;

    @BeforeAll
    static void setUpLogger() throws Exception {
        Logger logger = Mockito.mock(Logger.class);
        Mockito.when(logger.isInfoEnabled()).thenReturn(false);
        setFinalStatic(ReceiptFilterCommand.class.getDeclaredField("log"), logger);
    }

    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    @BeforeEach
    void setUp() {
        initParameterMap();
        initRequestStorage();
    }

    private static void initRequestStorage() {
        Map<String, Object> requestStorage = new ConcurrentHashMap<>();

        requestStorage.put("receipts", ReceiptListUtil.getListReceipt());

        doAnswer(invocationOnMock -> {
            String key = invocationOnMock.getArgument(0, String.class);
            Object value = invocationOnMock.getArgument(1, Object.class);
            System.out.println(key);
            requestStorage.put(key, value);

            return null;
        }).when(req).setAttribute(anyString(), any());

        doAnswer(invocationOnMock -> {
            String key = invocationOnMock.getArgument(0, String.class);

            return requestStorage.get(key);
        }).when(req).getAttribute(anyString());
    }

    private static void initParameterMap() {
        Map<String, String[]> mapRequestParam = req.getParameterMap();
        mapRequestParam.put("filtering", new String[]{"true"});

        when(req.getParameterMap()).thenReturn(mapRequestParam);
    }

    @Test
    public void testReceiptCode_ShouldBeOK() throws IOException {
        Map<String, String[]> reqParameterMap = req.getParameterMap();
        reqParameterMap.put("creatorName", new String[]{"username"});

        Command receiptFilterCommand = new ReceiptFilterCommand();
        receiptFilterCommand.execute(req, resp);

        List<Receipt> expected = ReceiptListUtil.getListReceipt().subList(0, 4);

        assertEquals(expected, req.getAttribute("receipts"));
    }

    @Test
    public void testReceiptCode2_ShouldBeOK() throws IOException {
        Map<String, String[]> reqParameterMap = req.getParameterMap();
        reqParameterMap.put("creatorName", new String[]{"username2"});

        Command receiptFilterCommand = new ReceiptFilterCommand();
        receiptFilterCommand.execute(req, resp);

        List<Receipt> expected = ReceiptListUtil.getListReceipt().subList(4, 5);

        assertEquals(expected, req.getAttribute("receipts"));
    }

    @Test
    public void testMinPriceAndMaxPrice_ShouldBeOK() throws IOException {
        Map<String, String[]> reqParameterMap = req.getParameterMap();
        reqParameterMap.put("minPrice", new String[]{"5000"});
        reqParameterMap.put("maxPrice", new String[]{"20000"});

        Command receiptFilterCommand = new ReceiptFilterCommand();
        receiptFilterCommand.execute(req, resp);

        List<Receipt> expected = ReceiptListUtil.getListReceipt().subList(1, 4);

        assertEquals(expected, req.getAttribute("receipts"));
    }

    @Test
    public void testMinPriceAndMaxPriceAndIsCanceledAndIsDone_ShouldBeOK() throws IOException {
        Map<String, String[]> reqParameterMap = req.getParameterMap();
        reqParameterMap.put("minPrice", new String[]{"5000"});
        reqParameterMap.put("maxPrice", new String[]{"40000"});
        reqParameterMap.put("isDone", new String[]{"true"});
        reqParameterMap.put("isCanceled", new String[]{"true"});

        Command receiptFilterCommand = new ReceiptFilterCommand();
        receiptFilterCommand.execute(req, resp);

        List<Receipt> expected = ReceiptListUtil.getListReceipt().subList(4, 5);

        assertEquals(expected, req.getAttribute("receipts"));
    }

    @Test
    public void testMinPriceAndMaxPriceAndIsCanceledAndIsDone2_ShouldBeOK() throws IOException {
        Map<String, String[]> reqParameterMap = req.getParameterMap();
        reqParameterMap.put("minPrice", new String[]{"5000"});
        reqParameterMap.put("maxPrice", new String[]{"40000"});
        reqParameterMap.put("isDone", new String[]{"false"});
        reqParameterMap.put("isCanceled", new String[]{"true"});

        Command receiptFilterCommand = new ReceiptFilterCommand();
        receiptFilterCommand.execute(req, resp);

        //Empty
        List<Receipt> expected = ReceiptListUtil.getListReceipt().subList(2, 4);

        assertEquals(expected, req.getAttribute("receipts"));
    }

}