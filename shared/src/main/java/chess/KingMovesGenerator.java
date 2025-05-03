package chess;

public class KingMovesGenerator extends MovesGenerator {
    public KingMovesGenerator(ChessBoard board, ChessPosition position) {
        super(board, position);
        possibleMoves.add(new ChessPosition(position.getRow()-1,position.getColumn()-1));
        possibleMoves.add(new ChessPosition(position.getRow()-1, position.getColumn()));
        possibleMoves.add(new ChessPosition(position.getRow()-1,position.getColumn()+1));
        possibleMoves.add(new ChessPosition(position.getRow(),position.getColumn()-1));
        possibleMoves.add(new ChessPosition(position.getRow(),position.getColumn()+1));
        possibleMoves.add(new ChessPosition(position.getRow()+1,position.getColumn()-1));
        possibleMoves.add(new ChessPosition(position.getRow()+1, position.getColumn()));
        possibleMoves.add(new ChessPosition(position.getRow()+1,position.getColumn()+1));
        for(ChessPosition newPosition : possibleMoves) {
            if(newPosition.isInBounds() && (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != ownColor)) {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
    }
}
