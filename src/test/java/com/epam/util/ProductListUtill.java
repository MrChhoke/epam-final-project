package com.epam.util;

import com.epam.cash.register.entity.Product;

import java.util.List;

public final class ProductListUtill {

    public static List<Product> getListProducts(){
        return List.of(
                new Product(
                        1,
                        "code-1",
                        "ukrTitle_1",
                        "engTitle_1",
                        100,
                        5000
                ),
                new Product(
                        2,
                        "code-2",
                        "ukrTitle_2",
                        "engTitle_2",
                        30,
                        1000
                ),
                new Product(
                        3,
                        "code-3",
                        "ukrTitle_3",
                        "engTitle_3",
                        10,
                        10000
                ),
                new Product(
                        4,
                        "code-4",
                        "ukrTitle_4",
                        "engTitle_4",
                        0,
                        99_999
                ),
                new Product(
                        5,
                        "code-5",
                        "ukrTitle_5",
                        "engTitle_5",
                        0,
                        99_999
                )
        );
    }

}
