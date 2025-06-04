package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import request.ListGamesRequest;
import server.ResponseException;
import server.ServerFacade;

import java.lang.module.FindException;

public class InGameClient implements Client {
    private final String authToken;
    private final String username;
    private final Integer gameId;
    private final ChessGame.TeamColor color;

    public InGameClient(String authToken, String username, Integer gameId, ChessGame.TeamColor color) {
        this.authToken = authToken;
        this.username = username;
        this.gameId = gameId;
        this.color = color;
    }

    @Override
    public ClientSwitchRequest processInput(String input, ServerFacade facade) {
        drawBoard(color, facade);
        return new ClientSwitchRequest(authToken, username, null, null);
    }

    @Override
    public void printHelp() {

    }

    public void drawBoard(ChessGame.TeamColor color, ServerFacade facade) {
        if(color == null) {
            drawBoard(ChessGame.TeamColor.WHITE, facade);
            return;
        }
        try {
            var games = facade.listGames(new ListGamesRequest(authToken)).games();
            ChessBoard board = null;
            for (var gameData : games) {
                if (gameData.gameID().equals(gameId)) {
                    board = gameData.game().getBoard();
                }
            }
            if(board == null) {
                throw new FindException("No chess board found for provided game id.");
            }
            if(color == ChessGame.TeamColor.WHITE) {
                drawBoardLetters(false);
                drawBoardBody(false, board);
                drawBoardLetters(false);
            } else {
                drawBoardLetters(true);
                drawBoardBody(true, board);
                drawBoardLetters(true);
            }
        } catch (ResponseException e) {
            printError(e.getMessage().replaceAll("Error: ", ""));
        } catch (Exception e) {
            printError(e.getMessage());
        }

    }

    private void drawBoardLetters(boolean reversed) {
        if(reversed) {
            System.out.print(EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.EMPTY);
            for(int i = 7; i >= 0; i--) {
                System.out.printf(" %c ", 'a' + i);
            }
            System.out.println(EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_DARK_GREY);
        } else {
            System.out.print(EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.EMPTY);
            for(int i = 0; i < 8; i++) {
                System.out.printf(" %c ", 'a' + i);
            }
            System.out.println(EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_DARK_GREY);
        }
    }

    private void drawBoardBody(boolean reversed, ChessBoard board) {
        if(reversed) {
            for(int row = 1; row <= 8; row++) {
                drawRow(row, true, board);
            }
        } else {
            for(int row = 8; row > 0; row--) {
                drawRow(row, false, board);
            }
        }
    }

    private boolean drawPiece(int row, int col, boolean whiteBackground, ChessBoard board) {
        if(whiteBackground) {
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK);
            whiteBackground = false;
        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE);
            whiteBackground = true;
        }
        String pieceStr;
        try {
            pieceStr = board.getPiece(new ChessPosition(row, col)).toString();
        } catch (NullPointerException e) {
            pieceStr = " ";
        }
        System.out.printf(" %s ", pieceStr);
        return whiteBackground;
    }

    private void drawRow(int row, boolean reversed, ChessBoard board) {
        System.out.print(EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.printf(" %d ", row);
        if(reversed) {
            boolean whiteBackground = (row % 2 == 1);
            for(int col = 8; col >= 1; col--) {
                whiteBackground = drawPiece(row, col, whiteBackground, board);
            }
        } else {
            boolean whiteBackground = (row % 2 == 0);
            for(int col = 1; col <= 8; col++) {
                whiteBackground = drawPiece(row, col, whiteBackground, board);
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.printf(" %d ", row);
        System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY);
    }
}
