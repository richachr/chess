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

    public void checkAndAddPossibleMoves() {
        for(ChessPosition newPosition : possibleMoves) {
            if(newPosition.isInBounds() && (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != ownColor)) {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
    }

    Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}
