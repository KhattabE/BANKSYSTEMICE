package com.bankapp;

public class Deposit {

    // Attempts to deposit money into the account
    public void depositAmount(Account account, double amount) {
        account.deposit(amount);
    }

    // Creates a deposit transaction after money has been added
    public Transactions createDepositTransaction(Account account, double amount) {
        return new Transactions("DEPOSIT", amount, null, account);
    }




}