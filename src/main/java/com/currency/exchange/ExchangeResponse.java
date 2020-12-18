package com.currency.exchange;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeResponse {

    private BigDecimal amount;
    private String currencyCode;
}
