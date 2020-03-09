package com.example;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;

class RateProvider {
    private static final String urlAddress = "http://data.fixer.io/api/latest?access_key=9ee50a3fce7387f4f902f1f06594c4e5";

    public static BigDecimal getRate(Currency from, Currency to) throws IOException {
        JSONObject json = readJsonFromUrl();
        BigDecimal euroCurrencyFromRatio = new BigDecimal(json.getJSONObject("rates").getDouble(from.toString()));
        BigDecimal euroCurrencyToRatio = new BigDecimal(json.getJSONObject("rates").getDouble(to.toString()));
        return euroCurrencyToRatio.divide(euroCurrencyFromRatio, 4, RoundingMode.CEILING);
    }

    private static String readAll(BufferedReader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        return stringBuilder.toString();
    }

    private static JSONObject readJsonFromUrl() throws IOException, JSONException {
        InputStream inputStream = new URL(urlAddress).openStream();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = readAll(reader);
            JSONObject json = new JSONObject(jsonText);
            return json;
        }
        finally {
            inputStream.close();
        }
    }
}
