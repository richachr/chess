package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesGenerator {
    ArrayList<ChessMove> validMoves = new ArrayList<>();
    ArrayList<ChessPosition> possibleMoves = new ArrayList<>();
    ChessBoard board;
    ChessPosition position;

    public MovesGenerator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}
