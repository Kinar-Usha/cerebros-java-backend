package com.cerebros.exceptions;

public class NotEnoughHoldingsException extends RuntimeException {
    public NotEnoughHoldingsException() {
    }

    public NotEnoughHoldingsException(String s) {
    }

    public NotEnoughHoldingsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughHoldingsException(Throwable cause) {
        super(cause);
    }

    public NotEnoughHoldingsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
