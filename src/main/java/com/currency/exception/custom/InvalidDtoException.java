package com.currency.exception.custom;

public class InvalidDtoException extends RuntimeException {

    public InvalidDtoException(String message) {
        super(message);
    }

    public InvalidDtoException(String message, Throwable cause) {
        super(message, cause);
    }
}
