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






    // Login
    public void login() {
        ui.displayMsg("\n*** Login ***\n");

        //While true loop to keep lopping until the user enters the correct information
        while (true) {
            String username = ui.promptTxt("Enter your username:");
            String password = ui.promptTxt("Enter your password:");

            loggedInUser = db.loginUser(username, password);

            if (loggedInUser != null) {
                ui.displayMsg("Login successful! Welcome back " + loggedInUser.getUserName());
                chooseFromMenu();
                //Return will exit the while loop, since the conditions are met
                return;
            }

            //Here we give the user an option togo back, since the user might have pressed log in, but did not have log in yet,
            // and this is needed, so we dont get the user stuck in an endless while loop
            ui.displayError("Invalid username or password.");

            ui.displayMsg("1. Try again");
            ui.displayMsg("0. Back to start menu");

            int choice = ui.promptNumericInt("Choose an option:");

            if (choice == 0) {
                start();
            }
            // Else the loop will countinue
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
    public void chooseFromMenu() {
        boolean isLoggedIn = true;

        while ( isLoggedIn) {
            showMainMenu();

            int menuChoice = ui.promptNumericInt("Choose an option:");

            switch (menuChoice) {
                case 1 -> viewAccountInfo();
                case 2 -> showBalance();
                case 3 -> showTransactions();
                case 4 -> handleDeposit();
                case 5 -> handleWithdraw();
                case 8 -> {
                    logout();
                    isLoggedIn = false;
                }
                default -> ui.displayError("Invalid option");
            }
        }
    }



    //Method to show account Information
    public void viewAccountInfo(){
        db.userInformation();

    }





    //Method to logout
    public void logout() {
        //no one is logged in anymore
        loggedInUser = null;
        ui.displayMsg("Logged out successfully.");

        int logoutUserChoice = ui.promptNumericInt("""
                Would you like to go back to the Start Menu, or exist completely?
                1: Start menu: 
                2: Exist completely: 
                """);

        //If user chooses 1, then he will get back to the start menu
        if (logoutUserChoice == 1){
            start();
        } else if(logoutUserChoice == 2){
            System.exit(0);
        } else {
            ui.displayMsg("You can only choose 1 or 2, try again: ");
        }

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

