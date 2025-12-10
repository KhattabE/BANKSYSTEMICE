package com.bankapp;

import java.util.Scanner;

public class UIText {

    // Scanner for user input
    private Scanner scanner = new Scanner(System.in);

    // Shows a normal message instead of sout
    public void displayMsg(String msg) {
        System.out.println(msg);
    }

    // Shows an error message if error occurs
    public void displayError(String msg) {
        System.out.println("ERROR: " + msg);
    }

    // Prompts the user for a non-empty text
    public String promptTxt(String prompt) {
        displayMsg(prompt);
        String input = scanner.nextLine();

        while (input.trim().isEmpty()) {
            displayError("You must enter something!");
            input = scanner.nextLine();
        }

        return input;
    }

    // Prompts the user for a numeric input (returns a double)
    public double promptNumeric(String prompt) {
        while (true) {
            displayMsg(prompt);
            String input = scanner.nextLine();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                displayError("Invalid number, try again.");
            }
        }
    }
}