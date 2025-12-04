package com.bankapp;

public class Account {

    private int accountID;
    private double balance;
    private String accountType;
    private User owner;

    public Account(int accountID, double balance, String accountType, User owner) {
        this.accountID = accountID;
        this.balance = balance;
        this.accountType = accountType;
        this.owner = owner;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void deposit(double amount){
        if (amount >= 0){
            balance += amount;
        }
    }

    public boolean withdraw(double amount){
        if (amount > 0 && amount <= balance){
            balance -= amount;
            System.out.println("Withdrawel successfull");
            return true;
        }
        System.out.println("Withdrawel failed, try again later dumbass");
        return false;
    }

    public double getBalance() {
        return balance;
    }
}
