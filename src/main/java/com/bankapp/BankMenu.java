package com.bankapp;

import java.util.ArrayList;

public class BankMenu {

    private UIText ui;
    private User loggedInUser;

    // ONE database connection for the entire menu
    private DBconnection db;

    // Start Menu
    public void start() {
        ui = new UIText();

        // Create ONE db connection
        db = new DBconnection();
        db.connect("jdbc:sqlite:identifier.sqlite");

        ui.displayMsg("\n*** Welcome to AEMK Mobile Bank ***\n");

        boolean isRunning = true;
        while (isRunning) {
            ui.displayMsg("Start menu");
            ui.displayMsg("1. Log-in");
            ui.displayMsg("2. Create account");
            ui.displayMsg("0. Exit");

            int choice = (int) ui.promptNumeric("Choose an option:");

            switch (choice) {
                case 1 -> login();
                case 2 -> createAccount();
                case 0 -> isRunning = false;
                default -> ui.displayMsg("Invalid choice, please try again.");
            }
        }
    }

    // Create account
    public void createAccount() {
        ui.displayMsg("\n*** Create account ***\n");

        String firstName = ui.promptTxt("Enter your first name");
        while (firstName.length() < 3)
            firstName = ui.promptTxt("First name must be more than 2 characters!");

        String lastName = ui.promptTxt("Enter your last name");
        while (lastName.length() < 3)
            lastName = ui.promptTxt("Last name must be more than 2 characters!");

        String username = ui.promptTxt("Enter your username");
        while (username.length() < 3)
            username = ui.promptTxt("Username must be more than 2 characters!");

        String mail = ui.promptTxt("Enter your e-mail address");
        while (!isValidMail(mail))
            mail = ui.promptTxt("Please enter a valid email!: ");

        String password = ui.promptTxt("Enter your password");
        while (password.length() < 5)
            password = ui.promptTxt("Try again! The password must be at least 4 characters!");

        String phoneNumber = ui.promptTxt("Enter your phone number");
        while (!phoneNumber.matches("\\d{8,}"))
            phoneNumber = ui.promptTxt("Phone number must be at least 8 digits:");

        // Create user object
        User newUser = new User(1, username, firstName, lastName, mail, password, phoneNumber);

        // Save user using SAME db connection
        db.createBankAccountInformation(newUser);

        ui.displayMsg("Account successfully created for " + newUser.getUserName());
        ui.displayMsg("Welcome to AEMK " + newUser.getFirstName() + "!");
    }

    // LOGIN
    public void login() {
        ui.displayMsg("\n*** Login ***\n");

        String username = ui.promptTxt("Enter your username:");
        String password = ui.promptTxt("Enter your password:");

        // USE THE SAME DB INSTANCE
        loggedInUser = db.loginUser(username, password);

        if (loggedInUser != null) {
            ui.displayMsg("Login was successful! Welcome back, " + loggedInUser.getUserName());
            showMainMenu();
        } else {
            ui.displayError("Invalid username or password. Please try again.");
        }
    }

    // Mail validation
    public static boolean isValidMail(String mail) {
        if (mail == null) return false;

        return (mail.contains("@") && mail.contains(".") &&
                (mail.contains("live") || mail.contains("gmail")
                        || mail.contains("outlook") || mail.contains("hotmail")));
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

