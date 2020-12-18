package com.currency.exception;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeExceptionResponse {

    private Instant timestamp;
    private String message;
}
