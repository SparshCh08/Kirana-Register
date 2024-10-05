package com.kirana.register.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.kirana.register.model.ExchangeRatesResponse;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@Component
public class CurrencyConversionUtil {

    @Value("${currency.api.url}")
    private String apiUrl;

//    private final RedisTemplate<String, ExchangeRatesResponse> redisTemplate;

//    public CurrencyConversionUtil(RedisTemplate<String, ExchangeRatesResponse> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }

    // Fetch exchange rates and cache them for 3 days
//    @Cacheable(value = "currencyRates", key = "'rates'", cacheManager = "redisCacheManager")
    public ExchangeRatesResponse getExchangeRates() {
        RestTemplate restTemplate = new RestTemplate();
        ExchangeRatesResponse response = restTemplate.getForObject(apiUrl, ExchangeRatesResponse.class);

        // Store the response in Redis manually with a 3-day expiration
//        redisTemplate.opsForValue().set("rates", response, 3, TimeUnit.DAYS);

        return response;
    }

    // Convert currency based on the fetched exchange rates
    public BigDecimal convertCurrency(String to, String from, BigDecimal amount) {
        ExchangeRatesResponse response = getExchangeRates();

        if (response != null) {
            validateCurrencyAvailability(to, from, response);

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

    // Method to check if a currency is valid based on the available exchange rates
    public boolean isCurrencyValid(String currencyCode) {
        ExchangeRatesResponse response = getExchangeRates();
        return response != null && response.getRates().containsKey(currencyCode);
    }

    // Private method to validate if 'from' and 'to' currencies exist in the response
    private void validateCurrencyAvailability(String to, String from, ExchangeRatesResponse response) {
        if (!response.getRates().containsKey(from)) {
            throw new IllegalArgumentException("Currency '" + from + "' not found in the available rates");
        }
        if (!response.getRates().containsKey(to)) {
            throw new IllegalArgumentException("Currency '" + to + "' not found in the available rates");
        }
    }
}
