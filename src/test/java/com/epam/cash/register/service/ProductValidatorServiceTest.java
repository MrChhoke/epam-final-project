package com.epam.cash.register.service;


import com.epam.cash.register.entity.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ProductValidatorServiceTest {

    // You can change ProductValidatorService implimentation for different testing
    private static ProductValidatorService productValidatorService;

    @BeforeAll
    public static void initProductValidatorService() {
        productValidatorService = new ProductValidatorServiceImpl();
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void test(Product product, boolean expectedIsValid) {
        assertEquals(expectedIsValid, productValidatorService.isValid(product));
    }

    public static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Product1",
                                "Product1",
                                100L,
                                20D
                        ),
                        true
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "P",
                                "Product1",
                                100L,
                                20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "\n\n\n\t\t\t       ",
                                "Product1",
                                100L,
                                20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Products",
                                "     \n\n\n\t\t\t",
                                100L,
                                20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Products",
                                ",,,,,!",
                                100L,
                                20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Products",
                                "    ",
                                100L,
                                20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Product1",
                                "P",
                                100L,
                                20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Product1",
                                "Product1",
                                -100L,
                                20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Product1",
                                "Product1",
                                100L,
                                -20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Pr",
                                "Product1",
                                100L,
                                20D
                        ),
                        false
                ),
                Arguments.of(
                        new Product(
                                1,
                                "6b4521c9-ed01-448c-9e8e-a31a2011eb5f",
                                "Pro",
                                "Product1",
                                100L,
                                20D
                        ),
                        true
                )
        );
    }


}
