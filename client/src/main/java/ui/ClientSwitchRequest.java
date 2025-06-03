package ui;

import chess.ChessGame;

public record ClientSwitchRequest(String authToken, String username, Integer gameId, ChessGame.TeamColor playerColor) {
}
