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
        }catch (SQLException sqle){
            System.out.println("An error has occurred, we could not connect to the database!");
        }
    }





    //Method to insert the information about the user when they create an account
    public void createBankAccountInformation(int userId, String userName, String firstName, String lastName, String userMail, String userCode, String phoneNumber){
        String sql = """
                    INSERT INTO bankUser(user_id, user_name, first_name, last_name, user_mail, user_password, phoneNumber)
                    VALUES (?,?,?,?,?,?,?)
                    """;

        try{

            //What this does is that it lets us insert data safely into the sql query
            PreparedStatement pstmt = connection.prepareStatement(sql);

            //We insertthe variables into the sql here
            pstmt.setInt(1, userId);
            pstmt.setString(2, userName);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, userMail);
            pstmt.setString(6, userCode);
            pstmt.setString(7, phoneNumber);


            //And here we execute the insert
            pstmt.executeQuery();

            //And here we execute the insert
            pstmt.executeUpdate();


            //Just feedback to check if it is working
            System.out.println("Information has been added successfully!");


        }catch (SQLException sqle){
            System.out.println("Data could not be inserted into the database");
        }
    }

    //Method to get the userInformation if needed
    public void userInformation(){
        String sql = """
                SELECT user_id, user_name, first_name, last_name, user_mail, user_password, phoneNumber FROM bankUser
                """;

        try{
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);

            int userID = set.getInt("user_id");
            String userName = set.getString("user_name");
            String firstName = set.getString("first_name");
            String lastName = set.getString("last_name");
            String userMail = set.getString("user_mail");
            String userPassword = set.getString("user_password");
            String userPhoneNumber = set.getString("phoneNumber");

            System.out.println("User ID:" + " - " + userID +
                    "\nUser Name:" + " - " + userName +
                    "\nUser First Name:" + " - " + firstName +
                    "\nUser Last Name:" + " - " + lastName +
                    "\nUser Mail:" + " - " + userMail +
                    "\nUser Password:" + " - " + userPassword +
                    "\nUser Phone Number:" + " - " + userPhoneNumber);

        }catch (SQLException sqle){
            System.out.println("Could not retrive the data!");
        }

    }





    //Method to get the user login information from the database
    public void userLoginInformation(){
        String sql = """
                SELECT user_name, user_password FROM bankUser
                """;

        try{
            Statement stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);

            String userName = set.getString("user_name");
            String userPassword = set.getString("user_password");

            System.out.println(userName + " - " + userPassword);

        }catch (SQLException sqle){
            System.out.println("Could not retrive the data!");
        }

    }














}
