package com.bankapp;

public class Deposit {


    private UIText ui;
    private DBconnection db;

    // Constructor
    public Deposit(UIText ui, DBconnection db) {
        this.ui = ui;
        this.db = db;
    }

    // Deposit money into an account
    public void deposit(User loggedInUser) {
        if (loggedInUser == null) {
            ui.displayError("No user is logged in!");
            return;
        }

        ui.displayMsg("\n*** Deposit Money ***\n");

        // Show users accounts
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
}