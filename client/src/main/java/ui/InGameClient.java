package ui;

import chess.*;
import facade.ResponseException;
import facade.ServerFacade;
import websocket.ClientData;
import websocket.CoordinateHandler;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InGameClient implements Client {
    private final ClientData data;
    private ChessGame game;
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
                case "quit" -> leave(wsFacade);
                case "redraw" -> drawBoard(data.playerColor(), null);
                case "leave" -> {return leave(wsFacade);}
                case "move" -> makeMove(input, wsFacade);
                case "resign" -> resign(wsFacade);
                case "highlight" -> highlight(input);
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
        System.out.println(EscapeSequences.EMPTY + "move [from space] [to space] [promotion (optional)] -> " + EscapeSequences.SET_TEXT_ITALIC +
                "make a move, if valid." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "resign -> " + EscapeSequences.SET_TEXT_ITALIC +
                "forfeit the game." + EscapeSequences.RESET_TEXT_ITALIC);
        System.out.println(EscapeSequences.EMPTY + "highlight [space] -> " + EscapeSequences.SET_TEXT_ITALIC +
                "highlight legal moves for a piece." + EscapeSequences.RESET_TEXT_ITALIC);
    }

    private ClientData leave(WebSocketFacade facade) {
        try {
            facade.leave(data);
            System.out.println("You left the game.");
            return new ClientData(data.authToken(), data.username(), null, null);
        }  catch (ResponseException e) {
            printError(e.getMessage().replaceAll("Error: ", ""));
        }
        return null;
    }

    private void resign(WebSocketFacade facade) {
        try {
            facade.resign(data);
        }  catch (ResponseException e) {
            printError(e.getMessage().replaceAll("Error: ", ""));
        }
    }

    public void makeMove(String input, WebSocketFacade facade) {
        try(Scanner inputScanner = new Scanner(input)) {
            inputScanner.next();
            try {
                ChessPosition fromPosition = CoordinateHandler.getPosition(inputScanner.next());
                ChessPosition toPosition = CoordinateHandler.getPosition(inputScanner.next());
                ChessPiece.PieceType promotionType = null;
                if(inputScanner.hasNext()) {
                    promotionType = ChessPiece.PieceType.valueOf(inputScanner.next());
                }
                facade.makeMove(data, new ChessMove(fromPosition, toPosition, promotionType));
            } catch (NoSuchElementException e) {
                printError("Incorrect parameters; type \"help\" to list valid syntax.");
            } catch (Exception e) {
                printError(e.getMessage().replaceAll("Error: ", ""));
            }
        }
    }

    public void highlight(String input) {
        try(Scanner inputScanner = new Scanner(input)) {
            inputScanner.next();
            try {
                ChessPosition position = CoordinateHandler.getPosition(inputScanner.next());
                Collection<ChessMove> validMoves = game.validMoves(position);
                ArrayList<ChessPosition> positionToHighlight = new ArrayList<>();
                positionToHighlight.add(position);
                for(var move : validMoves) {
                    positionToHighlight.add(move.getEndPosition());
                }
                drawBoard(data.playerColor(), positionToHighlight);
            } catch (NoSuchElementException e) {
                printError("Incorrect parameters; type \"help\" to list valid syntax.");
            } catch (Exception e) {
                printError(e.getMessage().replaceAll("Error: ", ""));
            }
        }
    }

    public void loadGame(ChessGame game) {
        this.board = game.getBoard();
        drawBoard(data.playerColor(), null);
    }

    private void drawBoard(ChessGame.TeamColor color, Collection<ChessPosition> highlightedSpaces) {
        if(color == null) {
            drawBoard(ChessGame.TeamColor.WHITE, highlightedSpaces);
            return;
        }
        try {
            if(board == null) {
                throw new FindException("No chess board found for provided game id.");
            }
            if(color == ChessGame.TeamColor.WHITE) {
                drawBoardLetters(false);
                drawBoardBody(false, highlightedSpaces);
                drawBoardLetters(false);
            } else {
                drawBoardLetters(true);
                drawBoardBody(true, highlightedSpaces);
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

    private void drawBoardBody(boolean reversed, Collection<ChessPosition> highlightedSpaces) {
        if(reversed) {
            for(int row = 1; row <= 8; row++) {
                drawRow(row, true, getColumnsForRow(row, highlightedSpaces));
            }
        } else {
            for(int row = 8; row > 0; row--) {
                drawRow(row, false, getColumnsForRow(row, highlightedSpaces));
            }
        }
    }

    private boolean drawPiece(int row, int col, boolean whiteBackground, boolean highlighted) {
        if(whiteBackground) {
            if(highlighted) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREEN + EscapeSequences.SET_TEXT_COLOR_BLACK);
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK);
            }
            whiteBackground = false;
        } else {
            if(highlighted) {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN + EscapeSequences.SET_TEXT_COLOR_WHITE);
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE);
            }
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

    private void drawRow(int row, boolean reversed, Collection<Integer> highlightedColumns) {
        System.out.print(EscapeSequences.EMPTY + EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.printf(" %d ", row);
        boolean highlighted;
        if(reversed) {
            boolean whiteBackground = (row % 2 == 1);
            for(int col = 8; col >= 1; col--) {
                highlighted = highlightedColumns.contains(col);
                whiteBackground = drawPiece(row, col, whiteBackground, highlighted);
            }
        } else {
            boolean whiteBackground = (row % 2 == 0);
            for(int col = 1; col <= 8; col++) {
                highlighted = highlightedColumns.contains(col);
                whiteBackground = drawPiece(row, col, whiteBackground, highlighted);
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.printf(" %d ", row);
        System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY);
    }

    private Collection<Integer> getColumnsForRow(int row, Collection<ChessPosition> positions) {
        Collection<Integer> columns = new ArrayList<>();
        for(var position : positions) {
            if(position.getRow() == row) {
                columns.add(position.getColumn());
            }
        }
        return columns;
    }
}
