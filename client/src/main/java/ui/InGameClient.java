package ui;

import chess.ChessGame;
import server.ServerFacade;

import java.util.Scanner;

public class InGameClient implements Client {
    private String authToken;
    private String username;
    private Integer gameId;
    private ChessGame.TeamColor color;

    public InGameClient(String authToken, String username, Integer gameId, ChessGame.TeamColor color) {
        this.authToken = authToken;
        this.username = username;
        this.gameId = gameId;
        this.color = color;
    }

    @Override
    public ClientSwitchRequest processInput(String input, ServerFacade facade) {
        drawBoard(color);
        return new ClientSwitchRequest(authToken, username, null, null);
    }

    @Override
    public void printHelp() {

    }

    public void drawBoard(ChessGame.TeamColor color) {
        if(color == null) {
            drawBoard(ChessGame.TeamColor.WHITE);
        }
    }

}
