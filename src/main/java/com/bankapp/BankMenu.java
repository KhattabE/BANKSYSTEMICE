package com.bankapp;

import java.util.ArrayList;
import java.util.Scanner;

public class BankMenu {

    //Ui so we dont use println
    public UIText ui;

    //
    public User loggedInUser;

    //Arraylist to hold the bankMenu options
    private ArrayList<String> bankMenuOptions;


    //Getter method
    public ArrayList<String> getBankMenuOptions() {
        return bankMenuOptions;
    }


    //Method to connect to the database
    public void DBConnection(){
        //A variable to hold the database URL
        String databaseURL = ("jdbc:sqlite:identifier.sqlite");
        //Create a new instance of DBConnection
        DBconnection DBConnection = new DBconnection();
        //Connect to the database via the url
        DBConnection.connect(databaseURL);
    }



    //Method to start the menu
    public void start(){

    }


    //Method to login
    public void login(){

    }


    //Method to logout
    public void logout(){

    }


    //Method where we create the showMainMenu() options
    public void showMainMenu(){

    }

    //Method where user chooses the option
    public void chooseFromMenu(){

    }

    //
    public void handleDeposit(){

    }

    //
    public void handleWithdraw(){

    }

    //
    public void showBalance(){

    }

    //
    public void showTransactions(){

    }

}
