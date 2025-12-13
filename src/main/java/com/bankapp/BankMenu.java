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

    // Start Menu - First screen user sees
    public void start() {
        ui = new UIText();

        // Create ONE db connection
        db = new DBconnection();
        db.connect("jdbc:sqlite:identifier.sqlite");

        ui.displayMsg("\n*** Welcome to AEMK Mobile Bank ***\n");

        // Display start menu options
        ui.displayMsg("Start menu");
        ui.displayMsg("1. Log-in");
        ui.displayMsg("2. Create account");
        ui.displayMsg("0. Exit");

        int choice = ui.promptNumericInt("Choose an option:");

        // Handle user choice
        switch (choice) {
            case 1 -> login();
            case 2 -> createAccount();
            case 0 -> System.exit(0);
            default -> ui.displayMsg("Invalid choice, please try again.");
        }
    }


    // Create a new user account
    public void createAccount() {
        ui.displayMsg("\n*** Create account ***\n");

        // Get first name with validation
        String firstName = ui.promptTxt("Enter your first name");
        while (firstName.length() < 3)
            firstName = ui.promptTxt("First name must be more than 2 characters!");

        // Get last name with validation
        String lastName = ui.promptTxt("Enter your last name");
        while (lastName.length() < 3)
            lastName = ui.promptTxt("Last name must be more than 2 characters!");

        // Get username with validation
        String username = ui.promptTxt("Enter your username");
        while (username.length() < 3)
            username = ui.promptTxt("Username must be more than 2 characters!");

        // Get email with validation
        String mail = ui.promptTxt("Enter your e-mail address");
        while (!isValidMail(mail))
            mail = ui.promptTxt("Please enter a valid email!: ");

        // Get password with validation
        String password = ui.promptTxt("Enter your password");
        while (password.length() < 5)
            password = ui.promptTxt("Try again! The password must be at least 5 characters!");

        // Get phone number with validation
        String phoneNumber = ui.promptTxt("Enter your phone number");
        while (!phoneNumber.matches("\\d{8,}"))
            phoneNumber = ui.promptTxt("Phone number must be at least 8 digits:");

        // Create user object (0 = auto-generated ID)
        User newUser = new User(0, username, firstName, lastName, mail, password, phoneNumber);

        // Save user to database
        db.createBankAccountInformation(newUser);

        // Get the newly created user's ID by logging them in
        User createdUser = db.loginUser(username, password);

        // Automatically create a default bank account for new user
        if (createdUser != null) {
            db.createBalanceAccount(createdUser.getUserID(), "Main Account");
            ui.displayMsg("Default 'Main Account' created automatically!");
        }

        // Success messages
        ui.displayMsg("Account successfully created for " + newUser.getUserName());
        ui.displayMsg("Welcome to AEMK " + newUser.getFirstName() + "!");
        start();
    }


    // Login existing user
    public void login() {
        ui.displayMsg("\n*** Login ***\n");

        // Loop until successful login or user exits
        while (true) {
            String username = ui.promptTxt("Enter your username:");
            String password = ui.promptTxt("Enter your password:");

            // Attempt to login
            loggedInUser = db.loginUser(username, password);

            // If login successful, go to main menu
            if (loggedInUser != null) {
                ui.displayMsg("Login successful! Welcome back " + loggedInUser.getUserName() + "\n");
                chooseFromMenu();
                return;
            }

            // If login failed, give option to try again or go back
            ui.displayError("Invalid username or password.");
            ui.displayMsg("\nChoose an option:");
            ui.displayMsg("1. Try again");
            ui.displayMsg("0. Back to start menu");

            int choice = ui.promptNumericInt("");

            if (choice == 0) {
                start();
            }
            // Otherwise loop continues
        }
    }


    // Validate email format
    public static boolean isValidMail(String mail) {
        if (mail == null) return false;

        // Check for @ symbol, dot, and common email providers
        return (mail.contains("@") && mail.contains(".") &&
                (mail.contains("live") || mail.contains("gmail")
                        || mail.contains("outlook") || mail.contains("hotmail")));
    }


    // Display main menu options
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
                7: //Hvis der skal tilføjes flere ting, så husk at tilføje dem også til chooseFromMenu() Metoden
                8: Log-out  
                """);
        ui.displayMsg(mainMenuOptions.getFirst());
    }

    // Handle user's menu choice
    public void chooseFromMenu() {
        boolean isLoggedIn = true;

        // Keep showing menu until user logs out
        while (isLoggedIn) {
            showMainMenu();

            int menuChoice = ui.promptNumericInt("Choose an option:");

            // Execute chosen action
            switch (menuChoice) {
                case 1 -> viewAccountInfo();
                case 2 -> showBalance();
                case 3 -> showTransactions();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> bankAccount();
                case 8 -> {
                    logout();
                    isLoggedIn = false;
                }
                default -> ui.displayError("Invalid option");
            }
        }
    }


    // Display user's personal information
    public void viewAccountInfo() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        // Get and display user info from database
        db.userInformation(loggedInUser.getUserID());

        // Wait for user to exit
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


    // Show all bank accounts and their balances
    public void showBalance() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        // Display all accounts for this user
        db.userShowBalance(loggedInUser.getUserID());

        // Wait for user to exit
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

    // Display transaction history
    public void showTransactions() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        ui.displayMsg("\n*** Transaction History ***\n");

        // Get all transactions for this user
        db.getTransactionHistory(loggedInUser.getUserID());

        // Wait for user to exit
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

    // Deposit money into an account
    public void deposit() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        ui.displayMsg("\n*** Deposit Money ***\n");

        // Show user's accounts
        db.userShowBalance(loggedInUser.getUserID());

        // Get account ID to deposit into
        int accountId = ui.promptNumericInt("Enter the Account ID you want to deposit to:");

        // Verify account belongs to this user
        if (!db.validateAccountOwnership(loggedInUser.getUserID(), accountId)) {
            ui.displayError("Invalid account ID or you don't own this account!");
            return;
        }

        // Get current balance
        double currentBalance = db.getBalanceByAccountId(accountId);

        // Get deposit amount with validation
        double depositAmount = ui.promptNumericDouble("Enter amount to deposit:");

        while (depositAmount <= 0) {
            depositAmount = ui.promptNumericDouble("Amount must be greater than 0. Try again:");
        }

        // Calculate new balance
        double newBalance = currentBalance + depositAmount;

        // Update balance in database
        if (db.updateBalanceByAccountId(accountId, newBalance)) {
            ui.displayMsg("Deposit successful!");
            ui.displayMsg("Deposited: " + depositAmount + " kr");
            ui.displayMsg("New balance: " + newBalance + " kr");

            // Record transaction (0 for withdraw, depositAmount for deposit, 0 for transfer)
            db.addTransaction(accountId, 0, depositAmount, 0);
        } else {
            ui.displayError("Deposit failed!");
        }

        // Wait for user to exit
        while (true) {
            int choice = ui.promptNumericInt("0. Exit");
            if (choice == 0) {
                ui.displayMsg("Back to menu options:");
                return;
            } else {
                ui.displayError("Invalid input, try again.");
            }
        }
    }

    // Withdraw money from an account
    public void withdraw() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        ui.displayMsg("\n*** Withdraw Money ***\n");

        // Show user's accounts
        db.userShowBalance(loggedInUser.getUserID());

        // Get account ID to withdraw from
        int accountId = ui.promptNumericInt("Enter the Account ID you want to withdraw from:");

        // Verify account belongs to this user
        if (!db.validateAccountOwnership(loggedInUser.getUserID(), accountId)) {
            ui.displayError("Invalid account ID or you don't own this account!");
            return;
        }

        // Get and display current balance
        double currentBalance = db.getBalanceByAccountId(accountId);
        ui.displayMsg("Current balance: " + currentBalance + " kr");

        // Get withdrawal amount with validation
        double withdrawAmount = ui.promptNumericDouble("Enter amount to withdraw:");

        while (withdrawAmount <= 0) {
            withdrawAmount = ui.promptNumericDouble("Amount must be greater than 0. Try again:");
        }

        // Check if user has enough money
        if (withdrawAmount > currentBalance) {
            ui.displayError("Insufficient balance! You only have " + currentBalance + " kr");
            return;
        }

        // Calculate new balance
        double newBalance = currentBalance - withdrawAmount;

        // Update balance in database
        if (db.updateBalanceByAccountId(accountId, newBalance)) {
            ui.displayMsg("Withdrawal successful!");
            ui.displayMsg("Withdrawn: " + withdrawAmount + " kr");
            ui.displayMsg("New balance: " + newBalance + " kr");

            // Record transaction (withdrawAmount for withdraw, 0 for deposit, 0 for transfer)
            db.addTransaction(accountId, withdrawAmount, 0, 0);
        } else {
            ui.displayError("Withdrawal failed!");
        }

        // Wait for user to exit
        while (true) {
            int choice = ui.promptNumericInt("0. Exit");
            if (choice == 0) {
                ui.displayMsg("Back to menu options:");
                return;
            } else {
                ui.displayError("Invalid input, try again.");
            }
        }
    }


    // Create a new bank account
    public void bankAccount() {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        ui.displayMsg("*** Create a new bank account ***");

        // Get account name from user
        String accountName = ui.promptTxt("Enter name for your new bank account:");

        // Validate name is not empty
        while (accountName.trim().isEmpty()) {
            accountName = ui.promptTxt("Account name cannot be empty. Please enter a name:");
        }

        // Create account in database
        db.createBalanceAccount(loggedInUser.getUserID(), accountName);

        // Display success message
        ui.displayMsg("New bank account '" + accountName + "' created successfully for " + loggedInUser.getUserName());
        ui.displayMsg("Starting balance: 0.0 kr.");

        // Wait for user to exit
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


    // Log out current user
    public void logout() {
        // Clear logged in user
        loggedInUser = null;
        ui.displayMsg("Logged out successfully.");

        // Give option to return to start menu or exit completely
        int logoutUserChoice = ui.promptNumericInt("""
                Would you like to go back to the Start Menu, or exist completely?
                1: Start menu: 
                2: Exist completely: 
                """);

        // Handle user choice
        if (logoutUserChoice == 1) {
            start();
        } else if (logoutUserChoice == 2) {
            System.exit(0);
        } else {
            // Keep asking until valid choice
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


