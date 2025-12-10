package com.bankapp;

import java.util.Date;

public class Transactions {

    private int transactionID;
    private Date localDateTime;
    private String type;
    private double amount;
    private Account fromAccount;
    private Account toAccount;


    // Constructor
    public Transactions(String type, double amount, Account fromAccount, Account toAccount) {
        this.type = type;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.localDateTime = new Date(); // Time of transaction
    }


    //Getter methods
    public int getTransactionID() {
        return transactionID;
    }

    public Date getLocalDateTime() {
        return localDateTime;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }


    public String getSummary() {
        return "Type: " + type +
                ", Amount: " + amount +
                ", From: " + fromAccount +
                ", To: " + toAccount +
                ", Date: " + localDateTime;
    }






}
