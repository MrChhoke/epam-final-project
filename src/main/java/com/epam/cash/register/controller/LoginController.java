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

@WebServlet(urlPatterns = {"/login", "/log"})
public class LoginController extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp-pages/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> map = req.getParameterMap();

        if (userService.isUserExists(map.get("username")[0])) {
            User user = userService.findUserByName(map.get("username")[0]);
            if (user.getPassword().equals(map.get("password")[0])) {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect("/");
            }else{
                req.setAttribute("error","Password is incorrect");
                req.getRequestDispatcher("/WEB-INF/jsp-pages/login.jsp").forward(req, resp);
            }
        }else{
            req.setAttribute("error", "No user was found");
            req.getRequestDispatcher("/WEB-INF/jsp-pages/login.jsp").forward(req, resp);
        }
    }
}
