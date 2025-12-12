package com.bankapp;

import java.sql.*;

public class DBconnection {

    //This object is the connection that is needed to connect to the database
    Connection connection;


    //Method to be able to connect to the database
    public void connect(String dataURL){
        try{
            //The driver manager is what creates the Connection for us
            connection = DriverManager.getConnection(dataURL);
            System.out.println("Database connection successful!");
            System.out.println("Connected to: " + dataURL);
        }catch (SQLException sqle){
            System.out.println(" ERROR: Could not connect to the database!");
            System.out.println("Database URL: " + dataURL);
            sqle.printStackTrace(); // This will show us the actual error
        }
    }





    //Method to insert the information about the user when they create an account
    public void createBankAccountInformation(User user) {
        // Don't insert user_id - let SQLite auto-generate it with AUTOINCREMENT
        String sql = """
                INSERT INTO bankUser(user_name, first_name, last_name, user_mail, user_password, user_phoneNum)
                VALUES (?,?,?,?,?,?)
                """;

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);

            // Note: We removed user_id, so indices shift down by 1
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getUserEmail());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getPhoneNumber());

            pstmt.executeUpdate();

            System.out.println("Information has been added successfully!");

        } catch (SQLException sqle) {
            System.out.println("Data could not be inserted into the database");
            sqle.printStackTrace(); // Show the actual error
        }
    }


    //Method to get the userInformation if needed
    public void userInformation() {
        String sql = """
            SELECT user_id, user_name, first_name, last_name, user_mail, user_password, user_phoneNum 
            FROM bankUser
            """;

        try {
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);

            while (set.next()) {
                int userID = set.getInt("user_id");
                String userName = set.getString("user_name");
                String firstName = set.getString("first_name");
                String lastName = set.getString("last_name");
                String userMail = set.getString("user_mail");
                String userPassword = set.getString("user_password");
                String userPhoneNumber = set.getString("user_phoneNum");

                System.out.println("User ID: " + userID +
                        "\nUser Name: " + userName +
                        "\nUser First Name: " + firstName +
                        "\nUser Last Name: " + lastName +
                        "\nUser Mail: " + userMail +
                        "\nUser Password: " + userPassword +
                        "\nUser Phone Number: " + userPhoneNumber +
                        "\n---------------------------");
            }

        } catch (SQLException sqle) {
            System.out.println("Could not retrieve the data!");
            sqle.printStackTrace();
        }
    }






    //Method to get the user login information from the database
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
        return null; // if login fails
    }


    // Create a balance account for a new user
    public void createBalanceAccount(int userId) {
        String sql = "INSERT INTO userBalance(balance, bankuser_id) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, 0.0); // Initial balance of 0
            pstmt.setInt(2, userId);

            pstmt.executeUpdate();
            System.out.println("Balance account created successfully!");

        } catch (SQLException e) {
            System.out.println("Could not create balance account");
            e.printStackTrace();
        }
    }


    // Get the balance for a user
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


    // Update balance (for deposits and withdrawals)
    public boolean updateBalance(int userId, double newBalance) {
        String sql = "UPDATE userBalance SET balance = ? WHERE bankuser_id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Could not update balance");
            e.printStackTrace();
            return false;
        }
    }


    // Add a transaction record
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
            pstmt.setInt(4, balanceId);

            pstmt.executeUpdate();
            System.out.println("Transaction recorded!");

        } catch (SQLException e) {
            System.out.println("Could not record transaction");
            e.printStackTrace();
        }
    }


    // Get balance_id from user_id (needed for transactions table)
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


    // Get transaction history for a user
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

            while (rset.next()) {
                hasTransactions = true;
                int transId = rset.getInt("transactions_id");
                double withdraw = rset.getDouble("withdraw_transaction");
                double deposit = rset.getDouble("deposit_transaction");
                double transfer = rset.getDouble("transfer_transaction");

                System.out.println("\nTransaction ID: " + transId);
                if (withdraw > 0) System.out.println("  Withdrawal: $" + withdraw);
                if (deposit > 0) System.out.println("  Deposit: $" + deposit);
                if (transfer > 0) System.out.println("  Transfer: $" + transfer);
                System.out.println("  ---------------------------");
            }

            if (!hasTransactions) {
                System.out.println("No transactions found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve transaction history");
            e.printStackTrace();
        }
    }
}