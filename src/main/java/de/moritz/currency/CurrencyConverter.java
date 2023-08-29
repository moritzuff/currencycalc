package de.moritz.currency;

import java.net.http.HttpClient;

public class CurrencyConverter {
    private final CurrencySource currencySource;

    public CurrencyConverter() {
        currencySource = new CurrencySource(HttpClient.newHttpClient());
    }

    public CurrencySource currencySource() {
        return currencySource;
    }
}
