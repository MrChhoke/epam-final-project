package com.epam.cash.register.exception;

import java.io.IOException;

public class ProductNotFoundException extends IOException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
