package model;

import chess.ChessGame;
import com.google.gson.annotations.Expose;

public record GameData(@Expose Integer gameID,
                       @Expose String whiteUsername,
                       @Expose String blackUsername,
                       @Expose String gameName,
                       @Expose ChessGame game) {
    public boolean isTaken(String color) {
        switch(color.toUpperCase()) {
            case "WHITE" -> {return this.whiteUsername != null;}
            case "BLACK" -> {return this.blackUsername != null;}
            default -> throw new RuntimeException("Unexpected team color.");
        }
    }
}
