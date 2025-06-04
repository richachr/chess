package ui;

import chess.ChessGame;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import request.LogoutRequest;
import server.ResponseException;
import server.ServerFacade;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class LoggedInClient implements Client {
    private final String authToken;
    private final String username;
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
                case "quit" -> logout(facade);
                case "logout" -> {return logout(facade);}
                case "create" -> create(input, facade);
                case "list" -> list(facade);
                case "join" -> {return join(input, facade);}
                case "observe" -> {return observe(input, facade);}
                default -> printError("Unexpected command; type \"help\" to list valid commands.");
            }
        }
        return null;
    }

    @Override
    public void printHelp() {
        System.out.println(EscapeSequences.EMPTY + "help -> " + EscapeSequences.SET_TEXT_ITALIC +
                           "display help text." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "logout -> " + EscapeSequences.SET_TEXT_ITALIC +
                           "log out of current session." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "create [gameName] -> " + EscapeSequences.SET_TEXT_ITALIC +
                           "create a game with the specified name." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "list -> " + EscapeSequences.SET_TEXT_ITALIC +
                           "list all current games." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "join [gameNumber] [color] -> " + EscapeSequences.SET_TEXT_ITALIC +
                           "join a game as the specified color." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "observe [gameNumber] -> " + EscapeSequences.SET_TEXT_ITALIC +
                           "join a game as an observer." + EscapeSequences.RESET_TEXT_ITALIC);
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
                list(facade);
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
                var game = games[i - 1];
                System.out.printf("%d (%s): ", i, game.gameName());
                if(game.whiteUsername() != null) {
                    System.out.printf("White is being played by %s, ", game.whiteUsername());
                } else {
                    System.out.print("White is available to play, ");
                }
                if(game.blackUsername() != null) {
                    System.out.printf("black is being played by %s.\n", game.blackUsername());
                } else {
                    System.out.print("black is available to play.\n");
                }
            }
        }  catch (ResponseException e) {
            printError(e.getMessage().replaceAll("Error: ", ""));
        }
    }

    public ClientSwitchRequest join(String input, ServerFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            inputScanner.next();
            JoinGameRequest req;
            try {
                int gameId = inputScanner.nextInt();
                if(games == null) {
                    System.out.println("Joining game based on these IDs: ");
                    list(facade);
                }
                gameId = games[gameId - 1].gameID();
                req = new JoinGameRequest(gameId, inputScanner.next(), authToken);
                facade.joinGame(req);
                System.out.printf("Joined game as %s\n", req.playerColor());
                return new ClientSwitchRequest(authToken, username, gameId, ChessGame.TeamColor.valueOf(req.playerColor().toUpperCase()));
            } catch (NoSuchElementException e) {
                printError("Incorrect parameters; type \"help\" to list valid syntax.");
            } catch (ResponseException e) {
                printError(e.getMessage().replaceAll("Error: ", ""));
            } catch (ArrayIndexOutOfBoundsException e) {
                printError("Incorrect game number. Check game number using `list`.");
            }
        }
        return null;
    }

    public ClientSwitchRequest observe(String input, ServerFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            inputScanner.next();
            try {
                int gameId = inputScanner.nextInt();
                if(games == null) {
                    System.out.println("Observing game based on these IDs: ");
                    list(facade);
                }
                gameId = games[gameId - 1].gameID();
                return new ClientSwitchRequest(authToken, username, gameId, null);
            } catch (NoSuchElementException e) {
                printError("Incorrect parameters; type \"help\" to list valid syntax.");
            } catch (ArrayIndexOutOfBoundsException e) {
                printError("Incorrect game number. Check game number using `list`.");
            }
        }
        return null;
    }
}
