package de.moritz.currency;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CurrencyConverter currencyConverter = new CurrencyConverter();
        CurrencySource currencySource = currencyConverter.currencySource();

        Scanner in = new Scanner(System.in);

        System.out.println("Welche Währung willst du in eine andere umrechnen?");
        String srcCurrencyId = in.nextLine();
        Currency srcCurrency = currencySource.loadCurrency(srcCurrencyId);
        if (srcCurrency == null) {
            System.out.println("Die angegebene Währung konnte nicht geladen werden.");
            return;
        }

        System.out.println("In welche Währung soll umgerechnet werden?");
        String dstCurrencyId = in.nextLine();
        if (!srcCurrency.canExchange(dstCurrencyId)) {
            System.out.println("Die angegebene Währung konnte nicht geladen werden.");
            return;
        }

        System.out.println("Wie viel Geld soll umgerechnet werden?");
        double amount = in.nextDouble();

        double result = srcCurrency.convertTo(amount, dstCurrencyId);
        System.out.printf("%.2f %s sind %.2f %s.\n", amount, srcCurrencyId, result, dstCurrencyId);
    }
}
