package com.cerebros.exceptions;

public class OrderInvalidException extends Exception {
    public OrderInvalidException() {
    }

    public OrderInvalidException(String message) {
        super(message);
    }

    public OrderInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderInvalidException(Throwable cause) {
        super(cause);
    }

    public OrderInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
