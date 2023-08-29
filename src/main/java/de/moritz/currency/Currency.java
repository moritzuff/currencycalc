package de.moritz.currency;

import java.util.Map;

public class Currency {
    private final String id;
    private final Map<String, Double> exchangeRates;
    private final long loadedAt;

    public Currency(String id, Map<String, Double> exchangeRates) {
        this.id = id;
        this.exchangeRates = exchangeRates;
        this.loadedAt = System.currentTimeMillis();
    }

    public String id() {
        return id;
    }

    public Map<String, Double> exchangeRates() {
        return exchangeRates;
    }

    public long loadedAt() {
        return loadedAt;
    }

    public double convertTo(String dstId, double amount) {
        Double exchangeRate = exchangeRates.get(dstId);
        if (exchangeRate == null) {
            return Double.NaN;
        }
        return amount * exchangeRate;
    }

    public boolean canExchange(String targetId) {
        return exchangeRates.containsKey(targetId);
    }
}
