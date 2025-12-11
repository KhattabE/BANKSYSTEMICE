package com.bankapp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public void DBConnection() {
        //A variable to hold the database URL
        String databaseURL = ("jdbc:sqlite:identifier.sqlite");
        //Create a new instance of DBConnection
        DBconnection DBConnection = new DBconnection();
        //Connect to the database via the url
        DBConnection.connect(databaseURL);
    }


    //Method to start the menu
    public void start() {
        ui = new UIText();
        DBconnection db = new DBconnection();
        db.connect("jdbc:sqlite:identifier.sqlite");

        ui.displayMsg("\n*** Welcome to AEMK Mobile Bank ***\n");
        boolean isRunning = true;
        while (isRunning) {
            ui.displayMsg("Start menu");
            ui.displayMsg("1. Log-in");
            ui.displayMsg("2. Create account");
            ui.displayMsg("0. Exit");

            int choice = (int) ui.promptNumeric("Choose an option:");

            // a Switch case to handle the users choice
            switch (choice) {
                case 1 -> login(); // if 1 then Login to an existing account
                case 2 -> createAccount(); // if 2 then Create a new user account
                case 0 -> isRunning = false; // if 0 then Exit the program
                default -> ui.displayMsg("Invalid choice, please try again.");
            }
        }
    }

    // method to create an account
    public void createAccount() {
        ui.displayMsg("\n*** Create account ***\n");

        // Opretter navn, hvor der skal være minst 2 characters
        String firstName = ui.promptTxt("Enter your first name");
        while (firstName.length() < 3) {
            firstName = ui.promptTxt("First name must be more than 2 characters!");
        }

        // Opretter efternavn, hvor der skal være minst 2 characters
        String lastName = ui.promptTxt("Enter your last name");
        while (lastName.length() < 3) {
            lastName = ui.promptTxt("Last name must be more than 2 characters!");
        }

        // Opretter username, hvor der skal være minst 2 characters
        String username = ui.promptTxt("Enter your username");
        while (username.length() < 3) {
            username = ui.promptTxt("Username must be more than 2 characters!");
        }

        // Opretter mail, hvor en række kriterier mødes
        String mail = ui.promptTxt("Enter your e-mail address");
        while (!isValidMail(mail)) {
            mail = ui.promptTxt("Please enter a valid email!: ");
        }

        //creates mail and A while loop to make sure the users code is at least 4 chars long
        String password = ui.promptTxt("Enter your password");
        while (password.length() < 5) {
            password = ui.promptTxt("Try again! the password must be at least 4 characters! ");
        }

        // opretter nummer med kun digits
        String phoneNumber = ui.promptTxt("Enter your phone number");
        while (!phoneNumber.matches("\\d{8,}")) {
            phoneNumber = ui.promptTxt("Phone number must be at least 8 digits and numbers only:");
        }

        // ny bruger med alle oplysninger
        User newUser = new User(1, username, firstName, lastName, mail, password, phoneNumber);

        // orpetter forbindelse til databasen
        DBconnection db = new DBconnection();
        db.connect("jdbc:sqlite:identifier.sqlite");

        // Dette gemmer brugeren direkte i databasen
        db.createBankAccountInformation(newUser);

        ui.displayMsg("Account successfully created for " + newUser.getUserName());
        ui.displayMsg("Welcome to AEMK " + newUser.getFirstName() + "!");
        ui.promptTxt("Press ENTER to return to main menu");
    }




    // Method to login
    public void login() {
        ui.displayMsg("\n*** Log-in ***\n");

        // bruger indtaster username og kode
        String username = ui.promptTxt("Enter your username:");
        String password = ui.promptTxt("Enter your password:");

        // opretter forbindelse til databasen
        DBconnection db = new DBconnection();
        db.connect("jdbc:sqlite:identifier.sqlite");

        // tjekker om brugeren findes i databasen
        String sql = "SELECT * FROM bankUser WHERE user_name = ? AND user_password = ?";


        // try catch så den fanger fejl, hvis brugeren ik finde
        try {
            PreparedStatement pstmt = db.connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            // hvis bruger findes
            if (rs.next()) {
                ui.displayMsg("Login successful! " +
                        "\n Welcome back, " + username);

                ui.promptTxt("Press ENTER to continue to the main menu");
                showMainMenu();

            } else {
                ui.displayError("Invalid username or password. Please try again");
            }

            // Lukker de forskelluge ting
            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            ui.displayError("Database error during login");
            e.printStackTrace();
        }
    }


    public static boolean isValidMail(String mail) {
        if (mail == null) return false;

        return (mail.contains("@") && mail.contains(".") && mail.indexOf("@") < mail.lastIndexOf(".") &&
                (mail.contains("live") || mail.contains("gmail") || mail.contains("outlook") || mail.contains("hotmail")) &&
                !((mail.contains("gmail") && mail.indexOf("gmail") > mail.lastIndexOf(".")) ||
                        (mail.contains("live") && mail.indexOf("live") > mail.lastIndexOf(".")) ||
                        (mail.contains("outlook") && mail.indexOf("outlook") > mail.lastIndexOf(".")) ||
                        (mail.contains("hotmail") && mail.indexOf("hotmail") > mail.lastIndexOf("."))));
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

