package chess;

public class KnightMovesGenerator extends MovesGenerator {
    public KnightMovesGenerator(ChessBoard board, ChessPosition position) {
        super(board, position);
        int row = position.getRow();
        int col = position.getColumn();
        possibleMoves.add(new ChessPosition(row+2, col+1));
        possibleMoves.add(new ChessPosition(row+1, col+2));
        possibleMoves.add(new ChessPosition(row+2, col-1));
        possibleMoves.add(new ChessPosition(row+1, col-2));
        possibleMoves.add(new ChessPosition(row-2, col+1));
        possibleMoves.add(new ChessPosition(row-1, col+2));
        possibleMoves.add(new ChessPosition(row-2, col-1));
        possibleMoves.add(new ChessPosition(row-1, col-2));
        for(ChessPosition newPosition : possibleMoves) {
            if(newPosition.isInBounds() && (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != ownColor)) {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
    }
}
