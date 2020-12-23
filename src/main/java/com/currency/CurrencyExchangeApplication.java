package com.currency;

import com.currency.exchange.rate.ExchangeRateService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CurrencyExchangeApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(CurrencyExchangeApplication.class, args);
        applicationContext.getBean(ExchangeRateService.class).updateCurrencyExchangeRate();
    }

}
