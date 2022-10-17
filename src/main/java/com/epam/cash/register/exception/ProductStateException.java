package com.epam.cash.register.exception;

import java.io.IOException;

public class ProductStateException extends IOException {

    public ProductStateException() {
        super("The product has no correct state in fields. Please change your decision!");
    }

    public ProductStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductStateException(String message) {
        super(message);
    }
}