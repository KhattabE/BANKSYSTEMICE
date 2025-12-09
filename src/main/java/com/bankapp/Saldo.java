package com.bankapp;

public class Saldo {

    // Returns the current balance of the given account
    public double getBalance(Account account) {
        return account.getBalance();
    }

    // The balance as a string
    public String formatBalance(double value) {
        return String.format("Current balance is: %.2f kr.", value);
    }

    // Prints the balance for the account using the textUi
    public void printBalance(Account account, UIText ui) {
        double balance = getBalance(account);
        String message = formatBalance(balance);
        // Når ui er lavet = ui.displayMsg(message);
    }
}