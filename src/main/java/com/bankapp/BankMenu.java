package com.bankapp;

import java.util.ArrayList;

public class BankMenu {

    private UIText ui;
    private User loggedInUser;
    private ArrayList<String> mainMenuOptions;
    private DBconnection db;
    private Deposit depositHandler;
    private Withdraw withdrawHandler;


    public ArrayList<String> getMainMenuOptions() {
        return mainMenuOptions;
    }

    public void start() {
        ui = new UIText();

        db = new DBconnection();
        db.connect("jdbc:sqlite:identifier.sqlite");

        depositHandler = new Deposit(ui, db);
        withdrawHandler = new Withdraw(ui, db);

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
            password = ui.promptTxt("Try again! The password must be at least 5 characters!");

        String phoneNumber = ui.promptTxt("Enter your phone number");
        while (!phoneNumber.matches("\\d{8,}"))
            phoneNumber = ui.promptTxt("Phone number must be at least 8 digits:");

        User newUser = new User(0, username, firstName, lastName, mail, password, phoneNumber);

        db.createBankAccountInformation(newUser);

        User createdUser = db.loginUser(username, password);

        if (createdUser != null) {
            db.createBalanceAccount(createdUser.getUserID(), "Main Account");
            ui.displayMsg("Default 'Main Account' created automatically!");
        }

        ui.displayMsg("Account successfully created for " + newUser.getUserName());
        ui.displayMsg("Welcome to AEMK " + newUser.getFirstName() + "!");
        start();
    }


    public void login() {
        ui.displayMsg("\n*** Login ***\n");

        while (true) {
            String username = ui.promptTxt("Enter your username:");
            String password = ui.promptTxt("Enter your password:");

            loggedInUser = db.loginUser(username, password);

            if (loggedInUser != null) {
                ui.displayMsg("Login successful! Welcome back " + loggedInUser.getUserName() + "\n");
                chooseFromMenu();
                return;
            }

            ui.displayError("Invalid username or password.");
            ui.displayMsg("\nChoose an option:");
            ui.displayMsg("1. Try again");
            ui.displayMsg("0. Back to start menu");

            int choice = ui.promptNumericInt("");

            if (choice == 0) {
                start();
            }
        }
    }


    public static boolean isValidMail(String mail) {
        if (mail == null) return false;

        return (mail.contains("@") && mail.contains(".") &&
                (mail.contains("live") || mail.contains("gmail")
                        || mail.contains("outlook") || mail.contains("hotmail")));
    }


    public void showMainMenu() {
        ui.displayMsg("Bank Menu Options: ");

        mainMenuOptions = new ArrayList<>();

        mainMenuOptions.add("""
                1: View Account information
                2: Show Balance
                3: Show Transactions
                4: Deposit
                5: Withdraw 
                6: Create bank account
                8: Log-out  
                """);
        ui.displayMsg(mainMenuOptions.getFirst());
    }

    public void chooseFromMenu() {
        boolean isLoggedIn = true;

        while (isLoggedIn) {
            showMainMenu();

            int menuChoice = ui.promptNumericInt("Choose an option:");

            switch (menuChoice) {
                case 1 -> viewAccountInfo();
                case 2 -> showBalance();
                case 3 -> showTransactions();
                case 4 -> depositHandler.deposit(loggedInUser);
                case 5 -> withdrawHandler.withdraw(loggedInUser);
                case 6 -> bankAccount();
                case 8 -> {
                    logout();
                    isLoggedIn = false;
                }
                default -> ui.displayError("Invalid option");
            }
        }
    }


    public void viewAccountInfo() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        db.userInformation(loggedInUser.getUserID());

        while (true) {
            int choice = ui.promptNumericInt("0. Exit");

            if (choice == 0) {
                ui.displayMsg("Exiting...");
                ui.displayMsg("Back to menu options:");
                return;
            } else {
                ui.displayError("Invalid input, try again.");
            }
        }
    }


    public void showBalance() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        db.userShowBalance(loggedInUser.getUserID());

        while (true) {
            int choice = ui.promptNumericInt("0. Exit");

            if (choice == 0) {
                ui.displayMsg("Exiting...");
                ui.displayMsg("Back to menu options:");
                return;
            } else {
                ui.displayError("Invalid input, try again.");
            }
        }
    }

    public void showTransactions() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        ui.displayMsg("\n*** Transaction History ***\n");

        db.getTransactionHistory(loggedInUser.getUserID());

        while (true) {
            int choice = ui.promptNumericInt("0. Exit");
            if (choice == 0) {
                ui.displayMsg("Exiting...");
                ui.displayMsg("Back to menu options:");
                return;
            } else {
                ui.displayError("Invalid input, try again.");
            }
        }
    }


    public void bankAccount() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        ui.displayMsg("*** Create a new bank account ***");

        String accountName = ui.promptTxt("Enter name for your new bank account:");

        while (accountName.trim().isEmpty()) {
            accountName = ui.promptTxt("Account name cannot be empty. Please enter a name:");
        }

        db.createBalanceAccount(loggedInUser.getUserID(), accountName);

        ui.displayMsg("New bank account '" + accountName + "' created successfully for " + loggedInUser.getUserName());
        ui.displayMsg("Starting balance: 0.0 kr.");

        while (true) {
            int choice = ui.promptNumericInt("0. Exit");

            if (choice == 0) {
                ui.displayMsg("Exiting...");
                ui.displayMsg("Back to menu options:");
                return;
            } else {
                ui.displayError("Invalid input, try again.");
            }
        }
    }


    public void logout() {
        loggedInUser = null;
        ui.displayMsg("Logged out successfully.");

        int logoutUserChoice = ui.promptNumericInt("""
                Would you like to go back to the Start Menu, or exist completely?
                1: Start menu: 
                2: Exist completely: 
                """);

        if (logoutUserChoice == 1) {
            start();
        } else if (logoutUserChoice == 2) {
            System.exit(0);
        } else {
            while (logoutUserChoice != 1 && logoutUserChoice != 2) {
                logoutUserChoice = ui.promptNumericInt("""
                        "You can only choose 1 or 2, try again: "
                        1: Start menu: 
                        2: Exist completely: 
                        """);
            }
        }
    }
}