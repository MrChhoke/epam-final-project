package com.epam.cash.register.exception;

import java.io.IOException;

public class ReceiptNotFoundException extends IOException {

    public ReceiptNotFoundException(String message) {
        super(message);
    }

    public ReceiptNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
