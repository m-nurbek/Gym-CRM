package com.epam.util;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;
import java.util.Scanner;

@Component
public class Shell implements AutoCloseable {
    private static final String PROMPT = "shell> ";
    private static final Scanner scanner = new Scanner(System.in);

    public static final String CREATE = "create";
    public static final String FIND = "find";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String EXIT = "exit";
    public static final String HELP = "help";
    public static final String LIST = "list";

    public Shell() {
        writeOutput("Welcome to the Gym CRM system!");
        writeOutput("Type 'help' to see the list of available commands.");
    }

    public boolean isValidCommand(String input) {
        return equals(input, CREATE) || equals(input, FIND) || equals(input, UPDATE) || equals(input, DELETE) || equals(input, EXIT) || equals(input, HELP) || equals(input, LIST);
    }

    public String readInput() {
        writePrompt();
        return scanner.nextLine().trim();
    }

    public String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public Date readDate(String prompt) {
        String date = readInput(prompt);

        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return new Date(date);
        } else {
            writeOutput("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
            return readDate(prompt);
        }
    }

    public BigInteger readBigInteger(String prompt) {
        String input = readInput(prompt);

        if (input.matches("\\d+")) {
            return new BigInteger(input);
        } else {
            writeOutput("Invalid input. Please enter a number.");
            return readBigInteger(prompt);
        }
    }

    public boolean readBoolean(String prompt) {
        System.out.print(prompt);

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            return true;
        } else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
            return false;
        } else {
            writeOutput("Invalid input. Please enter 'y' or 'n'.");
            return readBoolean(prompt);
        }
    }

    public boolean equals(String input, String command) {
        return input.equalsIgnoreCase(command);
    }

    public void writeOutput(String output) {
        System.out.println(output);
    }

    public void writePrompt() {
        System.out.print(PROMPT);
    }

    public void printHelp() {
        writeOutput("============== HELP ==============");
        writeOutput("Available commands:");
        writeOutput("""
                        FIND - find an entity
                        CREATE - create a new entity
                        UPDATE - update an entity
                        DELETE - delete an entity
                        LIST - list all entities
                        EXIT - exit the application

                        Commands are case-insensitive. Use the command HELP to display this message.
               """);
    }

    public void listOptions(String... options) {
        for (int i = 0; i < options.length; i++) {
            writeOutput(i + 1 + ". " + options[i]);
        }
    }

    public int printAndGetOption(String... options) {
        listOptions(options);
        writeOutput("Choose a number or print 'q' to cancel:");
        String input = readInput("> ");

        if (input.equalsIgnoreCase("q")) {
            return -1;
        } else if (!input.matches("\\d+")) {
            optionNotFound();
            return printAndGetOption(options);
        }

        int optionNumber = Integer.parseInt(input);

        if (optionNumber < 1 || optionNumber > options.length) {
            optionNotFound();
            return printAndGetOption(options);
        }

        return optionNumber;
    }

    public void optionNotFound() {
        writeOutput("Option not found. Please try again.");
    }

    public void close() {
        scanner.close();
    }
}