package com.bankapp;

import java.util.ArrayList;
import java.util.List;

public class User {


    //Class fields
    private int userID;
    private String userName;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String password;
    private String phoneNumber;
    private List<Account> accounts;


    //Class constructor
    public User(int userID, String userName, String firstName, String lastName, String userEmail, String password, String phoneNumber) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
    }



    //Getters
    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Account> getAccounts() {
        return accounts;
    }


    //Method to check if the password is correct
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    //Method to add account to user
    public void addAccount(Account account) {
        accounts.add(account);
    }





}

