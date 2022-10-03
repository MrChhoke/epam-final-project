package com.epam.cash.register.controller;

import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.service.*;
import com.epam.cash.register.util.RequestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/products")
public class ProductController extends HttpServlet {

    private static final Logger log = LogManager.getLogger(ProductController.class);

    private ProductService productService = new ProductServiceImpl();
    private UserService userService = new UserServiceImpl();
    private ReceiptService receiptService = new ReceiptServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("all_products", productService.findAll());

        String receiptCode = (String) req.getSession().getAttribute("receiptCode");
        Map<Long, Long> mapProductAtReceipt = new HashMap<>();

        if (receiptCode != null) {
            Receipt receipt = receiptService.findByCode(receiptCode);
            receipt.getItems().forEach(
                    itemReceipt -> mapProductAtReceipt.put(itemReceipt.getProduct().getId(), itemReceipt.getQuantity())
            );

        }
        req.setAttribute("mapProductAtReceipt", mapProductAtReceipt);
        req.getRequestDispatcher("/WEB-INF/jsp-pages/products.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> map = req.getParameterMap();

        User user = (User) req.getSession().getAttribute("user");

        Product product = new Product(
                0,
                map.get("code")[0],
                map.get("title_ukr")[0],
                map.get("title_eng")[0],
                Long.parseLong(map.get("quantity")[0]),
                Double.parseDouble(map.get("price")[0]),
                new Date(),
                user
        );
        productService.insert(product);
        resp.sendRedirect("/products");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long idDeletedProduct = -1;
        try {
            idDeletedProduct = Long.parseLong(req.getParameterMap().get("id")[0]);
        } catch (Throwable exp) {
            log.warn("The problem with id was found: ", exp);
        }
        if (idDeletedProduct != -1) {
            productService.delete(idDeletedProduct);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String> map = RequestUtil.getMapFromBody(req.getInputStream());


        User user = (User) req.getSession().getAttribute("user");
        Product product = new Product(
                Long.parseLong(map.get("id")),
                map.get("new_code"),
                map.get("new_title_ukr"),
                map.get("new_title_eng"),
                Long.parseLong(map.get("new_quantity")),
                Double.parseDouble(map.get("new_price")),
                new Date(),
                user
        );
        productService.update(product);
    }
}
