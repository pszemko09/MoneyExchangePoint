package com.example;

import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

class RateProvider {
    private static final String urlAddress = "http://data.fixer.io/api/latest?access_key=%s";

    public static BigDecimal getRate(Currency from, Currency to) throws IOException {
        JSONObject json = JSONReader.readJsonFromUrl(String.format(urlAddress, getKey("/key.pem")));
        BigDecimal euroCurrencyFromRatio = new BigDecimal(json.getJSONObject("rates").getDouble(from.toString()));
        BigDecimal euroCurrencyToRatio = new BigDecimal(json.getJSONObject("rates").getDouble(to.toString()));
        return euroCurrencyToRatio.divide(euroCurrencyFromRatio, 4, RoundingMode.CEILING);
    }

    public static String getKey(String keyFilename) throws IOException {
        InputStream inputStream = RateProvider.class.getResourceAsStream(keyFilename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String key = reader.readLine().trim();
        reader.close();
        return key;
    }
}
