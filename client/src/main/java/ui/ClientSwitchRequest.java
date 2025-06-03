package ui;

import chess.ChessGame;

public record ClientSwitchRequest(String authToken, Integer gameId, ChessGame.TeamColor playerColor) {
}
