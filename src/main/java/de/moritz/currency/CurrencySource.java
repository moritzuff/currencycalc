package de.moritz.currency;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CurrencySource {
    private static final long TIMEOUT_MILLIS = 1000 * 60 * 5;
    private final HttpClient httpClient;
    private final Currency[] buffer;
    private int index;

    public CurrencySource(HttpClient httpClient) {
        this(httpClient, 16);
    }

    public CurrencySource(HttpClient httpClient, int capacity) {
        this.httpClient = httpClient;
        this.buffer = new Currency[Math.max(capacity, 1)];
    }

    public Currency getOrFetch(String id) {
        for (Currency currency : buffer) {
            if (currency == null) {
                continue;
            }
            long time = System.currentTimeMillis();
            if (time - currency.loadedAt() > TIMEOUT_MILLIS) {
                continue;
            }
            if (!currency.id()
                .equals(id)) {
                continue;
            }
            return currency;
        }
        Currency currency = loadCurrency(id);
        if (currency == null) {
            return null;
        }
        index = (index + 1) % buffer.length;
        buffer[index] = currency;
        return currency;
    }

    public Currency loadCurrency(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder(
                    new URI("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/" + id + ".min.json"))
                .GET()
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return null;
            }
            JsonElement body = JsonParser.parseString(response.body());
            JsonObject jsonExchangeRates = body.getAsJsonObject()
                .get(id)
                .getAsJsonObject();
            Map<String, Double> exchangeRates = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : jsonExchangeRates.entrySet()) {
                exchangeRates.put(entry.getKey(), entry.getValue().getAsDouble());
            }
            return new Currency(id, exchangeRates);
        } catch (URISyntaxException | InterruptedException | IOException | JsonSyntaxException |
                 IllegalStateException e) {
            return null;
        }
    }
}
