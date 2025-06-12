package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import facade.ResponseException;
import facade.ServerFacade;
import websocket.ClientData;

import java.lang.module.FindException;
import java.util.Scanner;

public class InGameClient implements Client {
    private final ClientData data;
    private ChessBoard board;
    private WebSocketFacade wsFacade;

    public InGameClient(ClientData data, String url, NotificationHandler notificationHandler) {
        this.data = data;
        try {
            wsFacade = new WebSocketFacade(url, notificationHandler);
            wsFacade.connect(data);
        } catch (ResponseException e) {
            printError(e.getMessage());
        }
    }

    @Override
    public ClientData processInput(String input, ServerFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            switch(inputScanner.next().toLowerCase().strip()) {
                case "help" -> printHelp();
                case "redraw" -> drawBoard(data.playerColor());
//                case "leave" -> {return leave(facade);}
//                case "move" -> makeMove(input, facade);
//                case "resign" -> resign(facade);
//                case "highlight" -> highlight(input, facade);
                default -> printError("Unexpected command; type \"help\" to list valid commands.");
            }
        }
        return null;
    }

    @Override
    public void printHelp() {
        System.out.println(EscapeSequences.EMPTY + "help -> " + EscapeSequences.SET_TEXT_ITALIC +
                "display help text." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "redraw -> " + EscapeSequences.SET_TEXT_ITALIC +
                "redraw the current game board." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "leave -> " + EscapeSequences.SET_TEXT_ITALIC +
                "leave the current game." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "move [from space] [to space] -> " + EscapeSequences.SET_TEXT_ITALIC +
                "make a move, if valid." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "resign -> " + EscapeSequences.SET_TEXT_ITALIC +
                "forfeit the game." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "highlight [space] -> " + EscapeSequences.SET_TEXT_ITALIC +
                "highlight legal moves for a piece." + EscapeSequences.RESET_TEXT_ITALIC);
    }

    public void loadBoard(ChessBoard board) {
        this.board = board;
        drawBoard(data.playerColor());
    }

    private void drawBoard(ChessGame.TeamColor color) {
        if(color == null) {
            drawBoard(ChessGame.TeamColor.WHITE);
            return;
        }
        try {
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

    public ChessPosition getPositionFromCoordinate(String coordinate) throws Exception {
        char[] coordinateArray = coordinate.toCharArray();
        int row = 0;
        int col = 0;
        if(coordinateArray.length != 2) {
            throw new Exception("Incorrect coordinate length. Please try again.");
        }
        for(char coord : coordinateArray) {
            int coordInt = (int) coord;
            if(coordInt >= 49 && coordInt <= 56) {
                row = coordInt - 48;
            } else if (coordInt >= 65 && coordInt <= 72) {
                col = coordInt - 64;
            } else if (coordInt >= 97 && coordInt <= 104) {
                col = coordInt - 96;
            } else {
                throw new Exception("Incorrect coordinate entered. Please try again.");
            }
        }
        assert(row != 0 && col != 0);
        return new ChessPosition(row, col);
    }
}
