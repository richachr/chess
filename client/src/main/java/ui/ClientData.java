package ui;

import chess.ChessGame;

public record ClientData(String authToken, String username, Integer gameId, ChessGame.TeamColor playerColor) {
}
