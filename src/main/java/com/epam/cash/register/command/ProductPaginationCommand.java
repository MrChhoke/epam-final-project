package com.epam.cash.register.command;

import com.epam.cash.register.entity.Product;
import com.epam.cash.register.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductPaginationCommand implements Command {

    public static final int AMOUNT_PRODUCT_ON_PAGE = 10;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> parameterMap = RequestUtil.getSimpleParameterMap(req.getParameterMap());

        String numPageString = parameterMap.get("page");

        if (numPageString == null) {
            numPageString = "1";
        }

        Integer numPage = Integer.valueOf(numPageString);

        List<Product> products = (List<Product>) req.getAttribute("allProducts");

        req.setAttribute("amountPages", getAmountProductsPages((float) products.size() / AMOUNT_PRODUCT_ON_PAGE));
        req.setAttribute("numberCurrentPage", numPage);

        products = products.stream()
                .skip((long) AMOUNT_PRODUCT_ON_PAGE * (numPage - 1))
                .limit(AMOUNT_PRODUCT_ON_PAGE)
                .collect(Collectors.toList());

        req.setAttribute("allProducts", products);
    }

    private int getAmountProductsPages(double num) {
        if (Math.abs(num - Math.floor(num)) >= 0.000001) {
            return (int) (num + 1);
        }
        return (int) num;
    }
}
