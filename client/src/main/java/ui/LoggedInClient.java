package ui;

import model.GameData;
import request.CreateGameRequest;
import request.ListGamesRequest;
import request.LogoutRequest;
import server.ResponseException;
import server.ServerFacade;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class LoggedInClient implements Client {
    private String authToken;
    private String username;
    private GameData[] games;

    public LoggedInClient(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    @Override
    public ClientSwitchRequest processInput(String input, ServerFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            switch(inputScanner.next().toLowerCase().strip()) {
                case "help" -> printHelp();
                case "quit" -> {logout(facade);}
                case "logout" -> {return logout(facade);}
                case "create" -> {create(input, facade);}
                case "list" -> {list(facade);}
                // case "join" -> {return join(input, facade);}
                // case "observe" -> {return observe(input, facade);}
                default -> printError("Unexpected command; type \"help\" to list valid commands.");
            }
        }
        return null;
    }

    @Override
    public void printHelp() {
        System.out.println(EscapeSequences.EMPTY + "help -> " + EscapeSequences.SET_TEXT_ITALIC + "display help text." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "logout -> " + EscapeSequences.SET_TEXT_ITALIC + "log out of current session." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "create [gameName] -> " + EscapeSequences.SET_TEXT_ITALIC + "create a game with the specified name." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "list -> " + EscapeSequences.SET_TEXT_ITALIC + "list all current games." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "join [gameNumber] [color] -> " + EscapeSequences.SET_TEXT_ITALIC + "join a game as the specified color." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "observe [gameNumber] -> " + EscapeSequences.SET_TEXT_ITALIC + "join a game as an observer." + EscapeSequences.RESET_TEXT_ITALIC);
    }

    public ClientSwitchRequest logout(ServerFacade facade) {
        LogoutRequest req = new LogoutRequest(authToken);
        try {
            facade.logout(req);
            System.out.printf("Logged out as user %s\n", username);
            return new ClientSwitchRequest(null, null,null, null);
        }  catch (ResponseException e) {
            printError(e.getMessage().replaceAll("Error: ", ""));
        }
        return null;
    }

    public void create(String input, ServerFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            inputScanner.next();
            CreateGameRequest req;
            try {
                req = new CreateGameRequest(inputScanner.next(), authToken);
                facade.createGame(req);
                System.out.printf("Created new game: %s\n", req.gameName());
            } catch (NoSuchElementException e) {
                printError("Incorrect parameters; type \"help\" to list valid syntax.");
            } catch (ResponseException e) {
                printError(e.getMessage().replaceAll("Error: ", ""));
            }
        }
    }

    public void list(ServerFacade facade) {
        ListGamesRequest req = new ListGamesRequest(authToken);
        try {
            var res = facade.listGames(req);
            games = res.games();
            for(int i = 1; i <= games.length; i++) {
                System.out.printf("%d: ", i);
                System.out.println(games[i - 1].toString());
            }
        }  catch (ResponseException e) {
            printError(e.getMessage().replaceAll("Error: ", ""));
        }
    }
}
