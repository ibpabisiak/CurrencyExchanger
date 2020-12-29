package com.currency.exchange.rate.nbp;

import com.currency.exception.ExchangeExceptionMessage;
import com.currency.exception.custom.InvalidCurrencyCodeException;
import com.currency.exception.custom.NbpConnectionException;
import com.currency.exchange.rate.ExchangeRateService;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class NbpExchangeService implements ExchangeRateService {

    private final static String BASE_CURRENCY = "pln";
    private ConcurrentMap<String, BigDecimal> rates = new ConcurrentHashMap<>();
    private RestTemplate restTemplate = new RestTemplate();
    private Instant lastUpdate;

    @Override
    public BigDecimal getRateByCurrencyCode(String currencyCode) {
        if (BASE_CURRENCY.equals(currencyCode)) {
            return new BigDecimal("0");
        } else if (!getUnmodifiableRates().containsKey(currencyCode)) {
            throw new InvalidCurrencyCodeException(
                ExchangeExceptionMessage.INVALID_CURRENCY_CODE.formatWithParameter(currencyCode));
        }

        return getUnmodifiableRates().get(currencyCode);
    }

    @Override
    public boolean isBaseCurrency(String currencyCode) {
        return currencyCode.toLowerCase().equals(BASE_CURRENCY);
    }

    @Override
    public void updateCurrencyExchangeRate() {
        if (lastUpdate == null || !lastUpdate.truncatedTo(ChronoUnit.DAYS)
            .equals(Instant.now().truncatedTo(ChronoUnit.DAYS))) {
            lastUpdate = Instant.now();
            executeExchangeUpdate();
        }
    }

    private void executeExchangeUpdate() {
        log.info("Start currency exchange rate update.");
        NbpExchangeDto[] nbpExchangeDtoTableA = executeRestCall(NbpEndpointBuilder.nbpTableA());
        NbpExchangeDto[] nbpExchangeDtoTableB = executeRestCall(NbpEndpointBuilder.nbpTableB());

        if (nbpExchangeDtoTableA.length == 1 && nbpExchangeDtoTableB.length == 1) {
            log.info("Preparing exchange rates list");
            rates.clear();

            log.info("Table A effective date: {}", nbpExchangeDtoTableA[0].getEffectiveDate());
            nbpExchangeDtoTableA[0].getRates().forEach(r -> rates.put(r.getCode().toLowerCase(), r.getMid()));

            log.info("Table B effective date: {}", nbpExchangeDtoTableB[0].getEffectiveDate());
            nbpExchangeDtoTableB[0].getRates().forEach(r -> rates.put(r.getCode().toLowerCase(), r.getMid()));
        }

        log.info("Currency exchange rates (count: {}):\n{}", rates.size(), rates.toString());
        log.info("Currency exchange rate list has been loaded successfully");
    }

    private NbpExchangeDto[] executeRestCall(String nbpTableUrl) {
        try {
            log.info("GET REQUEST: {}", NbpEndpointBuilder.nbpTableA());
            ResponseEntity<NbpExchangeDto[]> response = Optional
                .of(restTemplate.getForEntity(nbpTableUrl, NbpExchangeDto[].class)).orElseThrow(
                    () -> new NbpConnectionException(ExchangeExceptionMessage.NBP_CONNECTION_ERROR
                        .formatWithParameter(NbpEndpointBuilder.nbpTableA())));
            log.info("GET RESPONSE: {}", response.getStatusCode());

            return Optional.ofNullable(response.getBody()).orElseThrow(() -> new NbpConnectionException(
                ExchangeExceptionMessage.NBP_BAD_RESPONSE.formatWithParameter(NbpEndpointBuilder.nbpTableA())));

        } catch (RestClientException e) {
            throw new NbpConnectionException(
                ExchangeExceptionMessage.NBP_CONNECTION_ERROR.formatWithParameter(NbpEndpointBuilder.nbpUrl()));
        }
    }

    private Map<String, BigDecimal> getUnmodifiableRates() {
        return Collections.unmodifiableMap(rates);
    }
}
