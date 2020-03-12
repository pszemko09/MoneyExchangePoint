package com.example;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class JSONReader {

    private static String readAll(BufferedReader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        return stringBuilder.toString();
    }

    public static JSONObject readJsonFromUrl(String urlAddress) throws IOException, JSONException {
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
