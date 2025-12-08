package com.bankapp;

import java.util.Date;

public class Transactions {

    private int transactionID;
    private Date localDateTime;
    private String type;
    private double amount;
    private Account fromAccount;
    private Account toAccount;


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
        return ""; //Vi skal tilføje noget her, men har bare lagt en placeholder for nu
   }




}
