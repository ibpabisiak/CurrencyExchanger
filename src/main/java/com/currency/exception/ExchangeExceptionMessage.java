package com.currency.exception;

import java.text.MessageFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExchangeExceptionMessage {
    INVALID_CURRENCY_CODE("INVALID_CURRENCY_CODE", "Following currency code is not supported: {0}"),
    NBP_CONNECTION_ERROR("NBP_CONNECTION_ERROR", "Could not connect to NBP API with following url: {0}"),
    NBP_BAD_RESPONSE("NBP_BAD_RESPONSE", "Bad response from {0}"),
    JSON_PROCESSING_ERROR("JSON_PROCESSING_ERROR", "Cannot prepare json from following object: {}");

    private final String errorCode;
    private final String message;

    public String formatWithParameter(String parameter) {
        return MessageFormat.format(getMessage(), parameter);
    }
}
