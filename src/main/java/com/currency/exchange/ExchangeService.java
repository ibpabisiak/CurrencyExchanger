package com.currency.exchange;

import com.currency.exchange.rate.ExchangeRateService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRateService exchangeRateService;

    public ExchangeResponse exchange(ExchangeDto exchangeDto, WebRequest request) {
        log.info("POST REQUEST FROM {}, BODY: {}", request.getDescription(false), exchangeDto.toJson());
        exchangeRateService.updateCurrencyExchangeRate();

        if (exchangeRateService.isBaseCurrency(exchangeDto.getSourceCurrencyCode())
            || exchangeRateService.isBaseCurrency(exchangeDto.getTargetCurrencyCode())) {

            return new ExchangeResponse(calculateAmountForBaseCurrency(exchangeDto),
                exchangeDto.getTargetCurrencyCode());
        } else {

            return new ExchangeResponse(calculateAmountForDifferentCurrencies(exchangeDto),
                exchangeDto.getTargetCurrencyCode());
        }
    }

    private BigDecimal calculateAmountForDifferentCurrencies(ExchangeDto exchangeDto) {

        BigDecimal baseRate = exchangeRateService.getRateByCurrencyCode(exchangeDto.getSourceCurrencyCode());
        BigDecimal targetRate = exchangeRateService.getRateByCurrencyCode(exchangeDto.getTargetCurrencyCode());
        BigDecimal rate = baseRate.divide(targetRate, 2, RoundingMode.HALF_UP);
        return exchangeDto.getAmount().multiply(rate);
    }

    private BigDecimal calculateAmountForBaseCurrency(ExchangeDto exchangeDto) {

        if (isBaseCurrencyAsSource(exchangeDto)) {
            BigDecimal targetRate = exchangeRateService.getRateByCurrencyCode(exchangeDto.getTargetCurrencyCode());
            return exchangeDto.getAmount().divide(targetRate, 2, RoundingMode.HALF_UP);

        } else if (isBaseCurrencyAsTarget(exchangeDto)) {
            BigDecimal baseRate = exchangeRateService.getRateByCurrencyCode(exchangeDto.getSourceCurrencyCode());
            return exchangeDto.getAmount().multiply(baseRate);
        }

        return exchangeDto.getAmount();
    }

    private boolean isBaseCurrencyAsSource(ExchangeDto exchangeDto) {
        return exchangeRateService.isBaseCurrency(exchangeDto.getSourceCurrencyCode())
            && !exchangeRateService.isBaseCurrency(exchangeDto.getTargetCurrencyCode());
    }

    private boolean isBaseCurrencyAsTarget(ExchangeDto exchangeDto) {
        return !exchangeRateService.isBaseCurrency(exchangeDto.getSourceCurrencyCode())
            && exchangeRateService.isBaseCurrency(exchangeDto.getTargetCurrencyCode());
    }

}
