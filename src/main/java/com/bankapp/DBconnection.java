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
            System.out.println("✓ Database connection successful!");
            System.out.println("Connected to: " + dataURL);
        }catch (SQLException sqle){
            System.out.println("✗ ERROR: Could not connect to the database!");
            System.out.println("Database URL: " + dataURL);
            sqle.printStackTrace(); // This will show us the actual error
        }
    }





    //Method to insert the information about the user when they create an account
    public void createBankAccountInformation(User user) {
        String sql = """
                INSERT INTO bankUser(user_id, user_name, first_name, last_name, user_mail, user_password, user_phoneNum)
                VALUES (?,?,?,?,?,?,?)
                """;

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setInt(1, user.getUserID());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, user.getUserEmail());
            pstmt.setString(6, user.getPassword());
            pstmt.setString(7, user.getPhoneNumber());

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
}