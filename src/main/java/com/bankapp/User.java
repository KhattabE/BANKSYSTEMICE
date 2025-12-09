package com.bankapp;

import java.util.List;

public class User {

    //Class fields
    private int userID;
    private String userName;
    private String FirstName;
    private String lastName;
    private String userEmail;
    private int password;
    private String phoneNumber;
    private List<Account> accounts;


    //Class constructor
    public User(int userID, String userName, String FirstName, String lastName, String userEmail, int password, String phoneNumber) {
        this.userID = userID;
        this.FirstName = FirstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
    }



    //Getters
    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Account> getAccounts() {
        return accounts;
    }



    //Method to check password
    public boolean checkPassword(String password){

    }





}

