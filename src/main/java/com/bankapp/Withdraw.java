package com.bankapp;

public class Withdraw {

    // Attempts to withdraw money using the Account class
    public boolean withdrawAmount(Account account, double amount) {
        return account.withdraw(amount);
    }

    // Creates a withdrawal transaction after a successful withdraw
    public Transactions createWithdrawTransaction(Account account, double amount) {
        return new Transactions("WITHDRAW", amount, account, null);
    }
}