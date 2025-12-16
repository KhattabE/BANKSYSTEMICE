package com.bankapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BankingAppGUI extends Application {

    private Stage primaryStage;
    private DBconnection db;
    private User loggedInUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.db = new DBconnection();
        db.connect("jdbc:sqlite:identifier.sqlite");

        primaryStage.setTitle("AEMK Mobile Bank");
        showStartScreen();
        primaryStage.show();
    }

    // Start Screen - Login or Create Account
    private void showStartScreen() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: #2C3E50;");

        Label title = new Label("Welcome to AEMK Mobile Bank");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");

        Button loginBtn = createStyledButton("Login", "#3498DB");
        Button createAccountBtn = createStyledButton("Create Account", "#2ECC71");
        Button exitBtn = createStyledButton("Exit", "#E74C3C");

        loginBtn.setOnAction(e -> showLoginScreen());
        createAccountBtn.setOnAction(e -> showCreateAccountScreen());
        exitBtn.setOnAction(e -> primaryStage.close());

        layout.getChildren().addAll(title, loginBtn, createAccountBtn, exitBtn);

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
    }

    // Login Screen
    private void showLoginScreen() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: #34495E;");

        Label title = new Label("Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: white;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E74C3C;");

        Button loginBtn = createStyledButton("Login", "#3498DB");
        Button backBtn = createStyledButton("Back", "#95A5A6");

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            loggedInUser = db.loginUser(username, password);

            if (loggedInUser != null) {
                showMainMenu();
            } else {
                errorLabel.setText("Invalid username or password!");
            }
        });

        backBtn.setOnAction(e -> showStartScreen());

        layout.getChildren().addAll(title, usernameField, passwordField, errorLabel, loginBtn, backBtn);

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
    }

    // Create Account Screen
    private void showCreateAccountScreen() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #34495E;");

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #34495E;");

        Label title = new Label("Create Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: white;");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name (min 3 characters)");
        firstNameField.setMaxWidth(300);

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name (min 3 characters)");
        lastNameField.setMaxWidth(300);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username (min 3 characters)");
        usernameField.setMaxWidth(300);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (min 5 characters)");
        passwordField.setMaxWidth(300);

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number (min 8 digits)");
        phoneField.setMaxWidth(300);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E74C3C;");

        Button createBtn = createStyledButton("Create Account", "#2ECC71");
        Button backBtn = createStyledButton("Back", "#95A5A6");

        createBtn.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String phone = phoneField.getText();

            // Validation
            if (firstName.length() < 3) {
                errorLabel.setText("First name must be at least 3 characters!");
                return;
            }
            if (lastName.length() < 3) {
                errorLabel.setText("Last name must be at least 3 characters!");
                return;
            }
            if (username.length() < 3) {
                errorLabel.setText("Username must be at least 3 characters!");
                return;
            }
            if (!isValidMail(email)) {
                errorLabel.setText("Please enter a valid email!");
                return;
            }
            if (password.length() < 5) {
                errorLabel.setText("Password must be at least 5 characters!");
                return;
            }
            if (!phone.matches("\\d{8,}")) {
                errorLabel.setText("Phone number must be at least 8 digits!");
                return;
            }

            // Create user
            User newUser = new User(0, username, firstName, lastName, email, password, phone);
            db.createBankAccountInformation(newUser);

            // Create default account
            User createdUser = db.loginUser(username, password);
            if (createdUser != null) {
                db.createBalanceAccount(createdUser.getUserID(), "Main Account");
            }

            showAlert("Success", "Account created successfully!", Alert.AlertType.INFORMATION);
            showStartScreen();
        });

        backBtn.setOnAction(e -> showStartScreen());

        layout.getChildren().addAll(title, firstNameField, lastNameField, usernameField,
                emailField, passwordField, phoneField, errorLabel, createBtn, backBtn);

        Scene scene = new Scene(scrollPane, 500, 600);
        primaryStage.setScene(scene);
    }

    // Main Menu Screen
    private void showMainMenu() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #2C3E50;");

        Label welcomeLabel = new Label("Welcome, " + loggedInUser.getUserName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setStyle("-fx-text-fill: white;");

        Button viewInfoBtn = createStyledButton("View Account Information", "#3498DB");
        Button showBalanceBtn = createStyledButton("Show Balance", "#9B59B6");
        Button showTransactionsBtn = createStyledButton("Show Transactions", "#1ABC9C");
        Button depositBtn = createStyledButton("Deposit", "#2ECC71");
        Button withdrawBtn = createStyledButton("Withdraw", "#E67E22");
        Button createAccountBtn = createStyledButton("Create Bank Account", "#F39C12");
        Button logoutBtn = createStyledButton("Logout", "#E74C3C");

        viewInfoBtn.setOnAction(e -> showAccountInfo());
        showBalanceBtn.setOnAction(e -> showBalance());
        showTransactionsBtn.setOnAction(e -> showTransactions());
        depositBtn.setOnAction(e -> showDepositScreen());
        withdrawBtn.setOnAction(e -> showWithdrawScreen());
        createAccountBtn.setOnAction(e -> showCreateBankAccountScreen());
        logoutBtn.setOnAction(e -> {
            loggedInUser = null;
            showStartScreen();
        });

        layout.getChildren().addAll(welcomeLabel, viewInfoBtn, showBalanceBtn, showTransactionsBtn,
                depositBtn, withdrawBtn, createAccountBtn, logoutBtn);

        Scene scene = new Scene(layout, 500, 600);
        primaryStage.setScene(scene);
    }

    // View Account Information
    private void showAccountInfo() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #34495E;");

        Label title = new Label("Account Information");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: white;");

        TextArea infoArea = new TextArea();
        infoArea.setEditable(false);
        infoArea.setPrefHeight(300);

        String info = String.format("""
            User ID: %d
            Username: %s
            First Name: %s
            Last Name: %s
            Email: %s
            Phone: %s
            """,
                loggedInUser.getUserID(),
                loggedInUser.getUserName(),
                loggedInUser.getFirstName(),
                loggedInUser.getLastName(),
                loggedInUser.getUserEmail(),
                loggedInUser.getPhoneNumber()
        );
        infoArea.setText(info);

        Button backBtn = createStyledButton("Back to Menu", "#95A5A6");
        backBtn.setOnAction(e -> showMainMenu());

        layout.getChildren().addAll(title, infoArea, backBtn);

        Scene scene = new Scene(layout, 500, 450);
        primaryStage.setScene(scene);
    }

    // Show Balance
    private void showBalance() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #34495E;");

        Label title = new Label("Your Bank Accounts");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: white;");

        TextArea balanceArea = new TextArea();
        balanceArea.setEditable(false);
        balanceArea.setPrefHeight(300);

        // Get balance info from database
        StringBuilder balanceInfo = new StringBuilder();
        try {
            String sql = "SELECT balance_id, balance, account_name FROM userBalance WHERE bankuser_id = ?";
            var pstmt = db.connection.prepareStatement(sql);
            pstmt.setInt(1, loggedInUser.getUserID());
            var rs = pstmt.executeQuery();

            while (rs.next()) {
                balanceInfo.append(String.format("""
                    Account Name: %s
                    Account ID: %d
                    Balance: %.2f kr
                    ---------------------------
                    
                    """,
                        rs.getString("account_name"),
                        rs.getInt("balance_id"),
                        rs.getDouble("balance")
                ));
            }

            if (balanceInfo.length() == 0) {
                balanceInfo.append("No accounts found.");
            }
        } catch (Exception ex) {
            balanceInfo.append("Error loading accounts.");
        }

        balanceArea.setText(balanceInfo.toString());

        Button backBtn = createStyledButton("Back to Menu", "#95A5A6");
        backBtn.setOnAction(e -> showMainMenu());

        layout.getChildren().addAll(title, balanceArea, backBtn);

        Scene scene = new Scene(layout, 500, 450);
        primaryStage.setScene(scene);
    }

    // Show Transactions
    private void showTransactions() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #34495E;");

        Label title = new Label("Transaction History");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: white;");

        TextArea transactionArea = new TextArea();
        transactionArea.setEditable(false);
        transactionArea.setPrefHeight(300);

        // Get transactions from database
        StringBuilder transInfo = new StringBuilder();
        try {
            String sql = """
                SELECT t.transactions_id, t.withdraw_transaction, t.deposit_transaction, 
                       t.transfer_transaction
                FROM transactions t
                JOIN userBalance ub ON t.transaction_balance_id = ub.balance_id
                WHERE ub.bankuser_id = ?
                ORDER BY t.transactions_id DESC
                """;
            var pstmt = db.connection.prepareStatement(sql);
            pstmt.setInt(1, loggedInUser.getUserID());
            var rs = pstmt.executeQuery();

            while (rs.next()) {
                transInfo.append(String.format("Transaction ID: %d\n", rs.getInt("transactions_id")));

                double withdraw = rs.getDouble("withdraw_transaction");
                double deposit = rs.getDouble("deposit_transaction");
                double transfer = rs.getDouble("transfer_transaction");

                if (withdraw > 0) transInfo.append(String.format("  Withdrawal: %.2f kr\n", withdraw));
                if (deposit > 0) transInfo.append(String.format("  Deposit: %.2f kr\n", deposit));
                if (transfer > 0) transInfo.append(String.format("  Transfer: %.2f kr\n", transfer));

                transInfo.append("---------------------------\n\n");
            }

            if (transInfo.length() == 0) {
                transInfo.append("No transactions found.");
            }
        } catch (Exception ex) {
            transInfo.append("Error loading transactions.");
        }

        transactionArea.setText(transInfo.toString());

        Button backBtn = createStyledButton("Back to Menu", "#95A5A6");
        backBtn.setOnAction(e -> showMainMenu());

        layout.getChildren().addAll(title, transactionArea, backBtn);

        Scene scene = new Scene(layout, 500, 450);
        primaryStage.setScene(scene);
    }

    // Deposit Screen
    private void showDepositScreen() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #34495E;");

        Label title = new Label("Deposit Money");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: white;");

        // Show user's accounts
        TextArea accountsArea = new TextArea();
        accountsArea.setEditable(false);
        accountsArea.setPrefHeight(150);
        accountsArea.setMaxWidth(400);

        StringBuilder accountsInfo = new StringBuilder("Your Accounts:\n\n");
        try {
            String sql = "SELECT balance_id, balance, account_name FROM userBalance WHERE bankuser_id = ?";
            var pstmt = db.connection.prepareStatement(sql);
            pstmt.setInt(1, loggedInUser.getUserID());
            var rs = pstmt.executeQuery();

            while (rs.next()) {
                accountsInfo.append(String.format("Account: %s\nID: %d | Balance: %.2f kr\n\n",
                        rs.getString("account_name"),
                        rs.getInt("balance_id"),
                        rs.getDouble("balance")
                ));
            }
        } catch (Exception ex) {
            accountsInfo.append("Error loading accounts.");
        }
        accountsArea.setText(accountsInfo.toString());

        TextField accountIdField = new TextField();
        accountIdField.setPromptText("Account ID");
        accountIdField.setMaxWidth(300);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount to deposit");
        amountField.setMaxWidth(300);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E74C3C;");

        Button depositBtn = createStyledButton("Deposit", "#2ECC71");
        Button backBtn = createStyledButton("Back", "#95A5A6");

        depositBtn.setOnAction(e -> {
            try {
                int accountId = Integer.parseInt(accountIdField.getText());
                double amount = Double.parseDouble(amountField.getText());

                if (amount <= 0) {
                    errorLabel.setText("Amount must be greater than 0!");
                    return;
                }

                if (!db.validateAccountOwnership(loggedInUser.getUserID(), accountId)) {
                    errorLabel.setText("Invalid account or you don't own this account!");
                    return;
                }

                double currentBalance = db.getBalanceByAccountId(accountId);
                double newBalance = currentBalance + amount;

                if (db.updateBalanceByAccountId(accountId, newBalance)) {
                    db.addTransaction(accountId, 0, amount, 0);
                    showAlert("Success", String.format("Deposited %.2f kr!\nNew balance: %.2f kr", amount, newBalance), Alert.AlertType.INFORMATION);
                    showMainMenu();
                } else {
                    errorLabel.setText("Deposit failed!");
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter valid numbers!");
            }
        });

        backBtn.setOnAction(e -> showMainMenu());

        layout.getChildren().addAll(title, accountsArea, accountIdField, amountField, errorLabel, depositBtn, backBtn);

        Scene scene = new Scene(layout, 500, 550);
        primaryStage.setScene(scene);
    }

    // Withdraw Screen
    private void showWithdrawScreen() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #34495E;");

        Label title = new Label("Withdraw Money");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: white;");

        // Show user's accounts
        TextArea accountsArea = new TextArea();
        accountsArea.setEditable(false);
        accountsArea.setPrefHeight(150);
        accountsArea.setMaxWidth(400);

        StringBuilder accountsInfo = new StringBuilder("Your Accounts:\n\n");
        try {
            String sql = "SELECT balance_id, balance, account_name FROM userBalance WHERE bankuser_id = ?";
            var pstmt = db.connection.prepareStatement(sql);
            pstmt.setInt(1, loggedInUser.getUserID());
            var rs = pstmt.executeQuery();

            while (rs.next()) {
                accountsInfo.append(String.format("Account: %s\nID: %d | Balance: %.2f kr\n\n",
                        rs.getString("account_name"),
                        rs.getInt("balance_id"),
                        rs.getDouble("balance")
                ));
            }
        } catch (Exception ex) {
            accountsInfo.append("Error loading accounts.");
        }
        accountsArea.setText(accountsInfo.toString());

        TextField accountIdField = new TextField();
        accountIdField.setPromptText("Account ID");
        accountIdField.setMaxWidth(300);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount to withdraw");
        amountField.setMaxWidth(300);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E74C3C;");

        Button withdrawBtn = createStyledButton("Withdraw", "#E67E22");
        Button backBtn = createStyledButton("Back", "#95A5A6");

        withdrawBtn.setOnAction(e -> {
            try {
                int accountId = Integer.parseInt(accountIdField.getText());
                double amount = Double.parseDouble(amountField.getText());

                if (amount <= 0) {
                    errorLabel.setText("Amount must be greater than 0!");
                    return;
                }

                if (!db.validateAccountOwnership(loggedInUser.getUserID(), accountId)) {
                    errorLabel.setText("Invalid account or you don't own this account!");
                    return;
                }

                double currentBalance = db.getBalanceByAccountId(accountId);

                if (amount > currentBalance) {
                    errorLabel.setText(String.format("Insufficient balance! You only have %.2f kr", currentBalance));
                    return;
                }

                double newBalance = currentBalance - amount;

                if (db.updateBalanceByAccountId(accountId, newBalance)) {
                    db.addTransaction(accountId, amount, 0, 0);
                    showAlert("Success", String.format("Withdrawn %.2f kr!\nNew balance: %.2f kr", amount, newBalance), Alert.AlertType.INFORMATION);
                    showMainMenu();
                } else {
                    errorLabel.setText("Withdrawal failed!");
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter valid numbers!");
            }
        });

        backBtn.setOnAction(e -> showMainMenu());

        layout.getChildren().addAll(title, accountsArea, accountIdField, amountField, errorLabel, withdrawBtn, backBtn);

        Scene scene = new Scene(layout, 500, 550);
        primaryStage.setScene(scene);
    }

    // Create Bank Account Screen
    private void showCreateBankAccountScreen() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #34495E;");

        Label title = new Label("Create New Bank Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: white;");

        TextField accountNameField = new TextField();
        accountNameField.setPromptText("Account Name (e.g., Savings, Checking)");
        accountNameField.setMaxWidth(300);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E74C3C;");

        Button createBtn = createStyledButton("Create Account", "#F39C12");
        Button backBtn = createStyledButton("Back", "#95A5A6");

        createBtn.setOnAction(e -> {
            String accountName = accountNameField.getText();

            if (accountName.trim().isEmpty()) {
                errorLabel.setText("Account name cannot be empty!");
                return;
            }

            db.createBalanceAccount(loggedInUser.getUserID(), accountName);
            showAlert("Success", "Bank account '" + accountName + "' created successfully!", Alert.AlertType.INFORMATION);
            showMainMenu();
        });

        backBtn.setOnAction(e -> showMainMenu());

        layout.getChildren().addAll(title, accountNameField, errorLabel, createBtn, backBtn);

        Scene scene = new Scene(layout, 500, 350);
        primaryStage.setScene(scene);
    }

    // Helper method to create styled buttons
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setMinWidth(250);
        button.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-cursor: hand;",
                color
        ));

        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.8;", "")));

        return button;
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Email validation
    private boolean isValidMail(String mail) {
        if (mail == null) return false;
        return (mail.contains("@") && mail.contains(".") &&
                (mail.contains("live") || mail.contains("gmail")
                        || mail.contains("outlook") || mail.contains("hotmail")));
    }
}