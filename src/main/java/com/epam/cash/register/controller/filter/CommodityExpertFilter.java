package com.epam.cash.register.controller.filter;

import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.ForbiddenException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/products/*")
public class CommodityExpertFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            throw new ForbiddenException("Please log in");
        }
        if (!(user.getRole() == User.Role.CASHIER ||
                user.getRole() == User.Role.COMMODITY_EXPERT ||
                user.getRole() == User.Role.ADMIN ||
                user.getRole() == User.Role.SENIOR_CASHIER)) {
            throw new ForbiddenException("You don`t have enough permissions");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
