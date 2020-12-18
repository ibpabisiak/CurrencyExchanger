package com.currency.exception.custom;

public class NbpConnectionException extends RuntimeException {

    public NbpConnectionException(String message) {
        super(message);
    }

    public NbpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
