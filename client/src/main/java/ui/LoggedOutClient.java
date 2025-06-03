package ui;

import request.LoginRequest;
import request.RegisterRequest;
import server.ResponseException;
import server.ServerFacade;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class LoggedOutClient implements Client {
    @Override
    public ClientSwitchRequest processInput(String input, ServerFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            switch(inputScanner.next().toLowerCase().strip()) {
                case "help" -> printHelp();
                case "quit" -> {}
                case "login" -> {return login(input, facade);}
                case "register" -> {return register(input, facade);}
                default -> printError("Unexpected command; type \"help\" to list valid commands.");
            }
        }
        return null;
    }

    @Override
    public void printHelp() {
        System.out.println(EscapeSequences.EMPTY + "help -> " + EscapeSequences.SET_TEXT_ITALIC + "display help text.");
        System.out.println(EscapeSequences.EMPTY + "quit -> " + EscapeSequences.SET_TEXT_ITALIC + "exit the program.");
        System.out.println(EscapeSequences.EMPTY + "login [username] [password] -> " + EscapeSequences.SET_TEXT_ITALIC + "log in as an existing user.");
        System.out.println(EscapeSequences.EMPTY + "register [username] [password] [email] -> " + EscapeSequences.SET_TEXT_ITALIC + "register a new user.");
        System.out.println(EscapeSequences.RESET_TEXT_ITALIC);
    }

    public ClientSwitchRequest login(String input, ServerFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            inputScanner.next();
            LoginRequest req;
            try {
                req = new LoginRequest(inputScanner.next(), inputScanner.next());
                var res = facade.login(req);
                return new ClientSwitchRequest(res.authToken(), null, null);
            } catch (NoSuchElementException e) {
                printError("Incorrect parameters; type \"help\" to list valid syntax.");
            } catch (ResponseException e) {
                printError(e.getMessage().replaceAll("Error: ", ""));
            }
        }
        return null;
    }

    public ClientSwitchRequest register(String input, ServerFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            inputScanner.next();
            RegisterRequest req;
            try {
                req = new RegisterRequest(inputScanner.next(), inputScanner.next(), inputScanner.next());
                var res = facade.register(req);
                return new ClientSwitchRequest(res.authToken(), null, null);
            } catch (NoSuchElementException e) {
                printError("Incorrect parameters; type \"help\" to list valid syntax.");
            } catch (ResponseException e) {
                printError(e.getMessage().replaceAll("Error: ", ""));
            }
        }
        return null;
    }
}
