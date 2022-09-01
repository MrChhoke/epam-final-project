package com.epam.cash.register.exception;

import javax.servlet.ServletException;

public class ForbiddenException extends ServletException {
    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public ForbiddenException(Throwable rootCause) {
        super(rootCause);
    }
}
