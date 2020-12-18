package com.currency.exchange;

import com.currency.exchange.rate.ExchangeRateService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

@Service
@Slf4j
@Data
public class ExchangeService {

    private final ExchangeRateService exchangeRateService;

    public ExchangeResponse exchange(ExchangeDto exchangeDto, WebRequest request) {
        log.info("POST REQUEST FROM {}, BODY: {}", request.getDescription(false), exchangeDto.toJson());
        exchangeRateService.updateCurrencyExchangeRate();
        return new ExchangeResponse(calculateAmount(exchangeDto), exchangeDto.getTargetCurrencyCode());
    }

    private BigDecimal calculateAmount(ExchangeDto exchangeDto) {
        BigDecimal baseRate = exchangeRateService.getRateByCurrencyCode(exchangeDto.getBaseCurrencyCode());
        BigDecimal targetRate = exchangeRateService.getRateByCurrencyCode(exchangeDto.getTargetCurrencyCode());

        if (exchangeRateService.getBaseCurrency().equals(exchangeDto.getBaseCurrencyCode())
            && exchangeRateService.getBaseCurrency().equals(exchangeDto.getTargetCurrencyCode())) {

            return exchangeDto.getAmount();
        } else if (exchangeRateService.getBaseCurrency().equals(exchangeDto.getTargetCurrencyCode())) {

            return exchangeDto.getAmount().divide(baseRate, 2, RoundingMode.HALF_UP);
        } else if (exchangeRateService.getBaseCurrency().equals(exchangeDto.getBaseCurrencyCode())) {

            return exchangeDto.getAmount().multiply(targetRate);
        } else {

            BigDecimal rate = baseRate.divide(targetRate, 2, RoundingMode.HALF_UP);
            return exchangeDto.getAmount().multiply(rate);
        }
    }

}
