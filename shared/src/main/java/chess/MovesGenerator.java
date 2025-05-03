package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesGenerator {
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    ArrayList<ChessPosition> possibleMoves = new ArrayList<>();
    ChessBoard board;
    ChessPosition position;
    ChessGame.TeamColor ownColor;

    public MovesGenerator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        ownColor = board.getPiece(position).getTeamColor();
    }

    Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}
