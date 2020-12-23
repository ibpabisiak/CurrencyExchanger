package com.currency.exchange.rate;

import java.math.BigDecimal;

public interface ExchangeRateService {

    void updateCurrencyExchangeRate();

    BigDecimal getRateByCurrencyCode(String currencyCode);

    boolean isBaseCurrency(String currencyCode);
}
