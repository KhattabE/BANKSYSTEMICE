package com.bankapp;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private int accountID;
    private double balance;
    private String accountType;
    private User owner;
    private List<Transactions> transactions;

    public Account(int accountID, double balance, String accountType, User owner) {
        this.accountID = accountID;
        this.balance = balance;
        this.accountType = accountType;
        this.owner = owner;
        this.transactions = new ArrayList<>();
    }

    //Getters
    public double getBalance() {
        return balance;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }



    //Method to deposit money to account
    public void deposit(double amount){
        if (amount >= 0){
            balance += amount;
        }
    }

    //Method to withdraw money from account
    public boolean withdraw(double amount){
        if (amount > 0 && amount <= balance){
            balance -= amount;
            System.out.println("Withdrawal happened successfully!");
            return true;
        }
        System.out.println("Withdrawal failed!");
        return false;
    }

    //Method to check if the account has enough money for a withdrawal or transfer
    public boolean canAfford(double amount){
        if(balance >= amount){
            System.out.println("You can afford to processed with this action");
            return true;
        } else {
            System.out.println("You can not afford to processed with this action");
            return false;
        }
    }


    //
    public void addTransaction(Transactions t) {
        transactions.add(t);
    }


    //Prints the transactions
    public void printTransactions() {
        for (Transactions t : transactions) {
            System.out.println(t.getSummary());
        }
    }

}
