package com.bankapp;

import java.sql.*;

public class DBconnection {

    // Connection object for database
    Connection connection;


    // Establish connection to database
    public void connect(String dataURL) {
        try {
            // Create connection using DriverManager
            connection = DriverManager.getConnection(dataURL);
            System.out.println("Database connection successful!");
            System.out.println("Connected to: " + dataURL);
        } catch (SQLException sqle) {
            System.out.println(" ERROR: Could not connect to the database!");
            System.out.println("Database URL: " + dataURL);
            sqle.printStackTrace();
        }
    }


    // Insert new user information into database
    public void createBankAccountInformation(User user) {
        String sql = """
                INSERT INTO bankUser(user_name, first_name, last_name, user_mail, user_password, user_phoneNum)
                VALUES (?,?,?,?,?,?)
                """;

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);

            // Set parameters for query
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getUserEmail());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getPhoneNumber());

            // Execute insert
            pstmt.executeUpdate();

            System.out.println("Information has been added successfully!");

        } catch (SQLException sqle) {
            System.out.println("Data could not be inserted into the database");
            sqle.printStackTrace();
        }
    }


    // Get and display user information by user ID
    public void userInformation(int userId) {
        String sql = """
                SELECT user_id, user_name, first_name, last_name, user_mail, user_password, user_phoneNum 
                FROM bankUser
                WHERE user_id = ?
                """;

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);

            ResultSet set = pstmt.executeQuery();

            // If user found, display their info
            if (set.next()) {
                int userID = set.getInt("user_id");
                String userName = set.getString("user_name");
                String firstName = set.getString("first_name");
                String lastName = set.getString("last_name");
                String userMail = set.getString("user_mail");
                String userPassword = set.getString("user_password");
                String userPhoneNumber = set.getString("user_phoneNum");

                System.out.println("\n----Your Account Information----");
                System.out.println("User ID: " + userID);
                System.out.println("User Name: " + userName);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + userMail);
                System.out.println("Phone Number: " + userPhoneNumber);
                System.out.println("------------------------------------\n");
            } else {
                System.out.println("User not found!");
            }

        } catch (SQLException sqle) {
            System.out.println("Could not retrieve the data!");
            sqle.printStackTrace();
        }
    }


    // Display all bank accounts and balances for a user
    public void userShowBalance(int userId) {
        String sql = """
    SELECT balance_id, balance, account_name 
    FROM userBalance
    WHERE bankuser_id = ?
    """;
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);

            ResultSet set = pstmt.executeQuery();

            System.out.println("*** All accounts and balance: ***\n");
            boolean hasAccounts = false;

            // Loop through all accounts
            while(set.next()){
                hasAccounts = true;
                int balance_id = set.getInt("balance_id");
                double balance = set.getDouble("balance");
                String accountName = set.getString("account_name");

                // Display account details
                System.out.println("Account Name: " + accountName);
                System.out.println("Account ID: " + balance_id);
                System.out.println("Balance: " + balance + " kr");
                System.out.println("---------------------------");
            }

            // If no accounts found
            if (!hasAccounts){
                System.out.println("No accounts found for this user");
                System.out.println("Create an account to view balance\n");
            }

        } catch (SQLException sqle) {
            System.out.println("Could not retrieve the data!");
        }
    }



    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM bankUser WHERE user_name = ? AND user_password = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rset = pstmt.executeQuery();


            if (rset.next()) {
                return new User(
                        rset.getInt("user_id"),
                        rset.getString("user_name"),
                        rset.getString("first_name"),
                        rset.getString("last_name"),
                        rset.getString("user_mail"),
                        rset.getString("user_password"),
                        rset.getString("user_phoneNum")
                );
            }

        } catch (SQLException e) {
            System.out.println("Login query failed");
            e.printStackTrace();
        }
        return null; // will return null if login fails
    }


    // Create a new bank account with name for a user
    public void createBalanceAccount(int userId, String accountName) {
        String sql = "INSERT INTO userBalance(balance, bankuser_id, account_name) VALUES (?, ?, ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, 0.0); // Starting balance is 0
            pstmt.setInt(2, userId);
            pstmt.setString(3, accountName);

            pstmt.executeUpdate();
            System.out.println("Balance account created successfully!");

        } catch (SQLException e) {
            System.out.println("Could not create balance account");
        }
    }


    // Get balance for a user (gets first account found)
    public double getBalance(int userId) {
        String sql = "SELECT balance FROM userBalance WHERE bankuser_id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);

            ResultSet rset = pstmt.executeQuery();

            if (rset.next()) {
                return rset.getDouble("balance");
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve balance");
            e.printStackTrace();
        }
        return 0.0; // Return 0 if no balance found
    }


    // Update balance for a user (updates first account found)
    public boolean updateBalance(int userId, double newBalance) {
        String sql = "UPDATE userBalance SET balance = ? WHERE bankuser_id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if update successful

        } catch (SQLException e) {
            System.out.println("Could not update balance");
            e.printStackTrace();
            return false;
        }
    }


    // Record a transaction in the database
    public void addTransaction(int balanceId, double withdraw, double deposit, double transfer) {
        String sql = """
                INSERT INTO transactions(withdraw_transaction, deposit_transaction, transfer_transaction, transaction_balance_id)
                VALUES (?, ?, ?, ?)
                """;

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, withdraw);
            pstmt.setDouble(2, deposit);
            pstmt.setDouble(3, transfer);
            pstmt.setInt(4, balanceId); // Link to specific account

            pstmt.executeUpdate();
            System.out.println("Transaction recorded!");

        } catch (SQLException e) {
            System.out.println("Could not record transaction");
            e.printStackTrace();
        }
    }


    // Get balance_id from user_id (gets first account found)
    public int getBalanceId(int userId) {
        String sql = "SELECT balance_id FROM userBalance WHERE bankuser_id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);

            ResultSet rset = pstmt.executeQuery();

            if (rset.next()) {
                return rset.getInt("balance_id");
            }

        } catch (SQLException e) {
            System.out.println("✗ Could not retrieve balance_id");
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }


    // Display transaction history for a user
    public void getTransactionHistory(int userId) {
        String sql = """
                SELECT t.transactions_id, t.withdraw_transaction, t.deposit_transaction, 
                t.transfer_transaction, t.transaction_balance_id
                FROM transactions t
                JOIN userBalance ub ON t.transaction_balance_id = ub.balance_id
                WHERE ub.bankuser_id = ?
                ORDER BY t.transactions_id DESC
                """;

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);

            ResultSet rset = pstmt.executeQuery();

            System.out.println("\n=== Transaction History ===");
            boolean hasTransactions = false;

            // Loop through all transactions
            while (rset.next()) {
                hasTransactions = true;
                int transId = rset.getInt("transactions_id");
                double withdraw = rset.getDouble("withdraw_transaction");
                double deposit = rset.getDouble("deposit_transaction");
                double transfer = rset.getDouble("transfer_transaction");

                // Display transaction details
                System.out.println("\nTransaction ID: " + transId);
                if (withdraw > 0) System.out.println("  Withdrawal: DKK" + withdraw);
                if (deposit > 0) System.out.println("  Deposit: DKK" + deposit);
                if (transfer > 0) System.out.println("  Transfer: DKK" + transfer);
                System.out.println("  ---------------------------");
            }

            // If no transactions found
            if (!hasTransactions) {
                System.out.println("No transactions found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve transaction history");
            e.printStackTrace();
        }
    }


    // Check if an account belongs to a specific user
    public boolean validateAccountOwnership(int userId, int accountId) {
        String sql = "SELECT balance_id FROM userBalance WHERE balance_id = ? AND bankuser_id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, userId);

            ResultSet rset = pstmt.executeQuery();
            return rset.next(); // Return true if account exists and belongs to user

        } catch (SQLException e) {
            System.out.println("Could not validate account ownership");
            e.printStackTrace();
            return false;
        }
    }

    // Get balance for a specific account by account ID
    public double getBalanceByAccountId(int accountId) {
        String sql = "SELECT balance FROM userBalance WHERE balance_id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountId);

            ResultSet rset = pstmt.executeQuery();

            if (rset.next()) {
                return rset.getDouble("balance");
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve balance");
            e.printStackTrace();
        }
        return 0.0; // Return 0 if not found
    }

    // Update balance for a specific account by account ID
    public boolean updateBalanceByAccountId(int accountId, double newBalance) {
        String sql = "UPDATE userBalance SET balance = ? WHERE balance_id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if update successful

        } catch (SQLException e) {
            System.out.println("Could not update balance");
            e.printStackTrace();
            return false;
        }
    }
}