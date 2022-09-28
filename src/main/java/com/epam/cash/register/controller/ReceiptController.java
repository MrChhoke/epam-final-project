package com.epam.cash.register.controller;

import com.epam.cash.register.entity.ItemReceipt;
import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.service.ProductService;
import com.epam.cash.register.service.ProductServiceImpl;
import com.epam.cash.register.service.ReceiptService;
import com.epam.cash.register.service.ReceiptServiceImpl;
import com.epam.cash.register.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/receipt")
public class ReceiptController extends HttpServlet {

    private ReceiptService receiptService;
    private ProductService productService;

    public ReceiptController() {
    }

    @Override
    public void init() {
        receiptService = new ReceiptServiceImpl();
        productService = new ProductServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Receipt> receipts = receiptService.findAll();

        req.getServletContext().setAttribute("receipts", receipts);

        req.getRequestDispatcher("WEB-INF/jsp-pages/receipt.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String> map = RequestUtil.getMapFromBody(req.getInputStream());

        String receiptCode = (String) req.getSession().getAttribute("receiptCode");

        User user = (User) req.getSession().getAttribute("user");


        if (map.containsKey("done")) {
            Receipt receipt = receiptService.findByCode(map.get("receipt_code"));
            receiptService.acceptReceipt(receipt);
            return;
        }

        if (receiptCode == null) {
            receiptCode = UUID.randomUUID().toString();
            req.getSession().setAttribute("receiptCode", receiptCode);
            Receipt receipt = new Receipt(
                    receiptCode,
                    user,
                    new ArrayList<>()
            );
            receipt.addItemReceipt(
                    new ItemReceipt(
                            0L,
                            Long.parseLong(map.get("quantityProduct")),
                            productService.findById(Long.parseLong(map.get("productID")))
                    )
            );
            receiptService.insert(receipt);
            return;
        }
        Receipt receipt = receiptService.findByCode(receiptCode);
        receipt.addItemReceipt(
                new ItemReceipt(
                        0L,
                        Long.parseLong(map.get("quantityProduct")),
                        productService.findById(Long.parseLong(map.get("productID")))
                )
        );
        receiptService.update(receipt);
    }
}
