package com.epam.cash.register.exception;

import java.io.IOException;

public class RegisteredUserException extends IOException {

    public RegisteredUserException() {
        super();
    }

    public RegisteredUserException(String message) {
        super(message);
    }

    public RegisteredUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisteredUserException(Throwable cause) {
        super(cause);
    }
}
