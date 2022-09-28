package com.epam.cash.register.exception;

import java.io.IOException;

public class ReceiptProcessedException extends IOException {

    public ReceiptProcessedException() {
        super();
    }

    public ReceiptProcessedException(String message) {
        super(message);
    }

    public ReceiptProcessedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReceiptProcessedException(Throwable cause) {
        super(cause);
    }
}
