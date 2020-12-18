package com.currency.exchange;

import com.currency.exception.ExchangeExceptionMessage;
import com.currency.exception.custom.InvalidDtoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExchangeDto {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String baseCurrencyCode;

    @NotNull
    private String targetCurrencyCode;

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new InvalidDtoException(
                ExchangeExceptionMessage.JSON_PROCESSING_ERROR.formatWithParameter(ExchangeDto.class.getName()));
        }
    }

}
