package com.epam.cash.register.command;

import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.User;
import com.epam.cash.register.exception.ProductStateException;
import com.epam.cash.register.service.ProductService;
import com.epam.cash.register.service.ProductServiceImpl;
import com.epam.cash.register.service.ProductValidatorService;
import com.epam.cash.register.service.ProductValidatorServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class ProductCreateCommand implements Command{

    ProductService productService = new ProductServiceImpl();
    ProductValidatorService productValidatorService = new ProductValidatorServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

        if(!productValidatorService.isValid(product)){
            throw new ProductStateException();
        }

        productService.insert(product);
    }
}
