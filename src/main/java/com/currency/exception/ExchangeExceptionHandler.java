package com.currency.exception;

import com.currency.exception.custom.InvalidCurrencyCodeException;
import com.currency.exception.custom.NbpConnectionException;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@ControllerAdvice
@Slf4j
public class ExchangeExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExchangeExceptionResponse> handleUnexpectedException(Exception ex,
        WebRequest request) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ExchangeExceptionResponse(Instant.now(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidCurrencyCodeException.class)
    public final ResponseEntity<ExchangeExceptionResponse> handleInvalidCurrencyCodeException(
        InvalidCurrencyCodeException ex,
        WebRequest request) {

        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .body(new ExchangeExceptionResponse(Instant.now(), ex.getMessage()));
    }

    @ExceptionHandler(NbpConnectionException.class)
    public final ResponseEntity<ExchangeExceptionResponse> handleNbpConnectionException(
        NbpConnectionException ex,
        WebRequest request) {

        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ExchangeExceptionResponse(Instant.now(), ex.getMessage()));
    }
}
