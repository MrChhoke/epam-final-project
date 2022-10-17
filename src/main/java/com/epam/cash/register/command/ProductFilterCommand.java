package com.epam.cash.register.command;

import com.epam.cash.register.entity.Product;
import com.epam.cash.register.entity.Receipt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductFilterCommand implements Command {

    private final static Logger log = LogManager.getLogger(ReceiptFilterCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Product> products = (List<Product>) req.getAttribute("allProducts");

        // if request is empty, this method will be skipped
        // because of there is nothing to filter
        if (products == null) {
            return;
        }

        String[] tempArr = req.getParameterMap().get("filtering");

        // if request hasn`t a field "filtering",
        // this method will be skipped because of
        // it isn`t filtering
        if (tempArr == null || !tempArr[0].equals("true")) {
            log.trace("filtering hasnt worked!");
            return;
        }

        Map<String, String> params = req.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        stringEntry -> stringEntry.getValue()[0])
                );


        String tempValue = null;

        final Long minQuantity = (tempValue = params.get("minQuantity")) != null ? Long.parseLong(tempValue) : null;
        final Long maxQuantity = (tempValue = params.get("maxQuantity")) != null ? Long.parseLong(tempValue) : null;
        final Double minPrice = (tempValue = params.get("minPrice")) != null ? Double.parseDouble(tempValue) : null;
        final Double maxPrice = (tempValue = params.get("maxPrice")) != null ? Double.parseDouble(tempValue) : null;

        final String ukranianTitle = params.get("ukranianTitle");
        final String englishTitle = params.get("englishTitle");

        Stream<Product> filteredProductsStream = products.stream();

        if (minQuantity != null) {
            log.trace("min quantity filtering");
            filteredProductsStream = filteredProductsStream.filter(product -> product.getQuantity() >= minQuantity);
        }

        if (maxQuantity != null) {
            log.trace("max quantity filtering");
            filteredProductsStream = filteredProductsStream.filter(product -> product.getQuantity() <= maxQuantity);
        }

        if (minPrice != null) {
            log.trace("min price filtering");
            filteredProductsStream = filteredProductsStream.filter(product -> product.getPrice() >= minPrice);
        }

        if (maxPrice != null) {
            log.trace("max price filtering");
            filteredProductsStream = filteredProductsStream.filter(product -> product.getPrice() <= maxPrice);
        }

        if (ukranianTitle != null) {
            log.trace("ukranian title filtering");
            filteredProductsStream = filteredProductsStream.filter(product -> product.getTitle_ukr().contains(ukranianTitle));
        }

        if (englishTitle != null) {
            log.trace("ukranian title filtering");
            filteredProductsStream = filteredProductsStream.filter(product -> product.getTitle_eng().contains(englishTitle));
        }


        List<Product> filteredProducts = filteredProductsStream.collect(Collectors.toList());

        log.trace("The size of filtered products` list is " + filteredProducts.size());
        req.setAttribute("allProducts", filteredProducts);
    }
}
