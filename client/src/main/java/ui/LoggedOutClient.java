package ui;

import server.ServerFacade;

import java.util.Scanner;

public class LoggedOutClient implements Client {
    @Override
    public void processInput(String input) {
        try(Scanner inputScanner = new Scanner(input)) {
            switch(inputScanner.next().toLowerCase().strip()) {
                case "help" -> printHelp();
                case "quit" -> {}
                case "login" -> login(input);
                case "register" -> register(input);
                default -> printError("Unexpected command; type \"help\" to list valid commands.");
            }
        }

    }

    @Override
    public void printHelp() {
        System.out.println(EscapeSequences.EMPTY + "help -> " + EscapeSequences.SET_TEXT_ITALIC + "display help text.");
        System.out.println(EscapeSequences.EMPTY + "quit -> " + EscapeSequences.SET_TEXT_ITALIC + "exit the program.");
        System.out.println(EscapeSequences.EMPTY + "login [username] [password] -> " + EscapeSequences.SET_TEXT_ITALIC + "log in as an existing user.");
        System.out.println(EscapeSequences.EMPTY + "register [username] [password] [email] -> " + EscapeSequences.SET_TEXT_ITALIC + "register a new user.");
        System.out.println(EscapeSequences.RESET_TEXT_ITALIC);
    }

    public void login(String input) {
        try(Scanner inputScanner = new Scanner(input)) {
            inputScanner.next();
            try {
                new ServerFacade()
            }
        }

    }

    public void register(String input) {

    }
}
