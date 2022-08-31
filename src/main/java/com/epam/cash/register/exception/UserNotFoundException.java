package com.epam.cash.register.exception;

import java.io.IOException;

public class UserNotFoundException extends IOException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
