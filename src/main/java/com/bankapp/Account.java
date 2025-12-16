package com.bankapp;

import java.util.ArrayList;
import java.util.List;

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

    //Getters
    public double getBalance() {
        return balance;
    }









}
