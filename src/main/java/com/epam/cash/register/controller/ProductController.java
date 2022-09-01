package com.epam.cash.register.controller;

import com.epam.cash.register.dao.PostqreSQLProductDAO;
import com.epam.cash.register.dao.ProductDAO;
import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.service.ProductService;
import com.epam.cash.register.service.ProductServiceImpl;
import com.epam.cash.register.service.UserService;
import com.epam.cash.register.service.UserServiceImpl;
import com.epam.cash.register.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/products")
public class ProductController extends HttpServlet {

    private ProductService productService = new ProductServiceImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/products.jsp").forward(req,resp);
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
        resp.sendRedirect("/products.jsp");
    }
}
