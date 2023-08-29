package de.moritz.currency;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CurrencyConverter currencyConverter = new CurrencyConverter();
        CurrencySource currencySource = currencyConverter.currencySource();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Welche Währung willst du in eine andere umrechnen?");
            String srcId = in.nextLine();
            Currency srcCurrency = currencySource.getOrFetch(srcId);
            if (srcCurrency == null) {
                System.out.println("Die angegebene Währung konnte nicht geladen werden.");
                continue;
            }

            System.out.println("In welche Währung soll umgerechnet werden?");
            String dstId = in.nextLine();
            if (!currencySource.canExchange(srcCurrency, dstId)) {
                System.out.println("Die angegebene Währung konnte nicht geladen werden.");
                continue;
            }

            System.out.println("Wie viel Geld soll umgerechnet werden?");
            double amount = in.nextDouble();
            in.nextLine();

            double result = currencySource.convertTo(srcCurrency, dstId, amount);
            System.out.printf("%.2f %s sind %.2f %s.\n", amount, srcId, result, dstId);
        }
    }
}
