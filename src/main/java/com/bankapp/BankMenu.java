package com.bankapp;

import java.util.ArrayList;

public class BankMenu {

    private UIText ui;
    private User loggedInUser;
    //Arraylist to store the main menu options in them
    private ArrayList<String> mainMenuOptions;
    // ONE database connection for the entire menu
    private DBconnection db;


    //Getter
    public ArrayList<String> getMainMenuOptions() {
        return mainMenuOptions;
    }

    // Start Menu
    public void start() {
        ui = new UIText();

        // Create ONE db connection
        db = new DBconnection();
        db.connect("jdbc:sqlite:identifier.sqlite");

        ui.displayMsg("\n*** Welcome to AEMK Mobile Bank ***\n");


            ui.displayMsg("Start menu");
            ui.displayMsg("1. Log-in");
            ui.displayMsg("2. Create account");
            ui.displayMsg("0. Exit");

            int choice = ui.promptNumericInt("Choose an option:");

            switch (choice) {
                case 1 -> login();
                case 2 -> createAccount();
                case 0 -> System.exit(0);
                default -> ui.displayMsg("Invalid choice, please try again.");
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
            ui.displayMsg("Login was successful! Welcome back " + loggedInUser.getUserName() + "!");
            chooseFromMenu();
        } else {
            ui.displayError("Invalid username or password! Please try again:");
        }
    }





    // Mail validation
    public static boolean isValidMail(String mail) {
        if (mail == null) return false;

        return (mail.contains("@") && mail.contains(".") &&
                (mail.contains("live") || mail.contains("gmail")
                        || mail.contains("outlook") || mail.contains("hotmail")));
    }







    //Method where we create the showMainMenu() options
    public void showMainMenu(){
        ui.displayMsg("Bank Menu Options: ");

        mainMenuOptions = new ArrayList<>();

        mainMenuOptions.add("""
                1: View Account information
                2: Show Balance
                3: Show Transactions
                4: Handle Deposit
                5: Handle Withdraw 
                6: //Hvis der skal tilføjes flere ting, så husk at tilføje dem også til chooseFromMenu() Metoden
                7: //Hvis der skal tilføjes flere ting, så husk at tilføje dem også til chooseFromMenu() Metoden
                8: Log-out  
                """);
        ui.displayMsg(mainMenuOptions.getFirst());

    }

    //Method where user chooses the option
    public void chooseFromMenu(){
        showMainMenu();

        ui.displayMsg("Choose one of following options: ");
        int menuChoice = ui.promptNumericInt("Choose an option:");

    }



    //Method to logout
    public void logout(){

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

