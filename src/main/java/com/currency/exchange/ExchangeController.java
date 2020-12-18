package com.currency.exchange;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchangerates")
@Slf4j
@Tag(name = "Currency Converter Controller")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @PostMapping
    public ResponseEntity<ExchangeResponse> convert(@Valid @RequestBody ExchangeDto exchangeDto, WebRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(exchangeService.exchange(exchangeDto, request));
    }


}
