package com.currency.exchange.rate.nbp;

import com.currency.exchange.rate.ExchangeRateDto;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NbpExchangeDto {

    private String table;
    private String no;
    private String effectiveDate;
    private List<ExchangeRateDto> rates;

}
