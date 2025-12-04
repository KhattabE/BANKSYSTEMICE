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


    public void insertIntoDataCreateAccount(String userName, String userCode){

        String sql = """
                INSERT INTO bankUser (user_name, user_password)
                VALUES (?,?);
                """;

        try{

            //What this does is that it lets us insert data safely into the sql query
            PreparedStatement pstmt = connection.prepareStatement(sql);


            //We insertthe variables into the sql here
            pstmt.setString(1, userName);
            pstmt.setString(2, userCode);

            pstmt.executeQuery();


            //And here we execute the insert
            pstmt.executeUpdate();

            //Just feedback to check if it is working
            System.out.println("Information has been added successfully!");



        }catch (SQLException sqle){
            System.out.println("Data could not be inserted into the database");
        }


    }



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
