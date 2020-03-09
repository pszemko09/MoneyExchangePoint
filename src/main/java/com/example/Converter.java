package com.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Converter {
    public static BigDecimal convert(Money moneyToConvert, Currency toCurrency) throws IOException {
        BigDecimal exchangeRate = RateProvider.getRate(moneyToConvert.getCurrency(), toCurrency);
        return BigDecimal.valueOf(moneyToConvert.getAmountOfMoney()).multiply(exchangeRate).setScale(4, RoundingMode.CEILING);
    }
}
