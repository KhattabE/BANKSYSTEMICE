
package com.bankapp;

public class Withdraw {

    private UIText ui;
    private DBconnection db;

    // Constructor
    public Withdraw(UIText ui, DBconnection db) {
        this.ui = ui;
        this.db = db;
    }

    // Withdraw money from an account
    public void withdraw(User loggedInUser) {
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

            // Record transaction
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
}