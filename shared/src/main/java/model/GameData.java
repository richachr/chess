package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public boolean isTaken(String color) {
        switch(color.toUpperCase()) {
            case "WHITE" -> {return this.whiteUsername != null;}
            case "BLACK" -> {return this.blackUsername != null;}
            default -> throw new RuntimeException("Unexpected team color.");
        }
    }
}
