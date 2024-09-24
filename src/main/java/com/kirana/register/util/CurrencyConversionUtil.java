package com.kirana.register.util;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.kirana.register.model.ExchangeRatesResponse;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class CurrencyConversionUtil {

    private static final String API_URL = "https://api.fxratesapi.com/latest";

    @Cacheable(value = "currencyRates")
    public ExchangeRatesResponse getExchangeRates() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, ExchangeRatesResponse.class);
    }

    public BigDecimal convertCurrency(String to, String from, BigDecimal amount) {
        ExchangeRatesResponse response = getExchangeRates();

        if (response != null) {
            if (response.getRates().containsKey(from) && response.getRates().containsKey(to)) {
                // Step 1: Convert from 'from' currency to USD
                BigDecimal amountInUSD = amount.divide(response.getRates().get(from), MathContext.DECIMAL128);

                // Step 2: Convert from USD to 'to' currency
                BigDecimal conversionRateTo = response.getRates().get(to);
                return amountInUSD.multiply(conversionRateTo).setScale(2, RoundingMode.HALF_UP);
            } else {
                throw new IllegalArgumentException("Currency not found in the available list");
            }
        } else {
            throw new IllegalArgumentException("Failed to retrieve exchange rates");
        }
    }
}
