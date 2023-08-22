package de.moritz.currency;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Currency {
    static JsonObject currencies;

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Scanner s = new Scanner(System.in);
        System.out.println("Welche Währung willst du in eine andere umwandeln?");
        String firstC = s.nextLine();
        System.out.println("Du hast " + firstC + " als erste Währung ausgewählt. In welche soll sie umgewandelt werden?");
        String secondC = s.nextLine();
        System.out.println("Du hast " + secondC + " als zweite Währung ausgewählt. Wie viel " + firstC + " sollen in " + secondC + " umgewandelt werden?");
        double amount = Double.parseDouble(s.nextLine());
        load(firstC);
        double result = amount * getCurrency(secondC);
        System.out.println(amount + " " + firstC + " sind " + result + " " + secondC);
    }

    public static void load(String currencyID) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest r = HttpRequest.newBuilder(new URI("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/" + currencyID + ".json"))
                .GET()
                .build();

        HttpClient c = HttpClient.newHttpClient();
        HttpResponse<String> res = c.send(r, HttpResponse.BodyHandlers.ofString());

        var body = JsonParser.parseString(res.body()).getAsJsonObject();
        currencies = body.get(currencyID).getAsJsonObject();
    }
    
    public static double getCurrency(String currencyID) {
        double currency = currencies.get(currencyID).getAsDouble();
        return currency;
    }
}
