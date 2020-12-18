package com.currency.exception.custom;

public class InvalidCurrencyCodeException extends RuntimeException {

    public InvalidCurrencyCodeException(String message) {
        super(message);
    }

    public InvalidCurrencyCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
