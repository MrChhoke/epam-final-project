package com.epam.cash.register.controller;

import com.epam.cash.register.entity.User;
import com.epam.cash.register.service.UserService;
import com.epam.cash.register.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = {"/reg", "/registration"})
public class RegisterController extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp-pages/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> map = req.getParameterMap();

        if (userService.isUserExists(map.get("username")[0])) {
            req.getServletContext().setAttribute("error", "Username is reserved");
            req.getRequestDispatcher("/WEB-INF/jsp-pages/registration.jsp").forward(req, resp);
        }

        User user = new User(
                0L,
                map.get("username")[0],
                map.get("password")[0],
                User.Role.CASHIER
        );
        userService.registerUser(user);
        req.getRequestDispatcher("/WEB-INF/jsp-pages/registration.jsp").forward(req, resp);
    }
}
