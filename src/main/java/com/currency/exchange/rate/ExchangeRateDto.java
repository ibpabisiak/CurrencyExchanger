package com.currency.exchange.rate;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExchangeRateDto {

    private String currency;
    private String code;
    private BigDecimal mid;
}