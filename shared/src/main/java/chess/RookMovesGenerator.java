package chess;

public class RookMovesGenerator extends MovesGenerator {
    public RookMovesGenerator(ChessBoard board, ChessPosition position) {
        super(board, position);
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition testPosition = new ChessPosition(position.getRow(), position.getColumn());
        // Check above
        row++;
        testPosition.setRow(row);
        while(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
            validMoves.add(new ChessMove(position,testPosition,null));
            row++;
            testPosition.setRow(row);
        }
        // Check below
        row = position.getRow() - 1;
        testPosition.setRow(row);
        while(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
            validMoves.add(new ChessMove(position,testPosition,null));
            row--;
            testPosition.setRow(row);
        }
        // Check right
        row = position.getRow();
        col++;
        testPosition.setRow(row);
        testPosition.setCol(col);
        while(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
            validMoves.add(new ChessMove(position,testPosition,null));
            col++;
            testPosition.setCol(col);
        }
        // Check left
        col = position.getColumn() - 1;
        testPosition.setCol(col);
        while(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
            validMoves.add(new ChessMove(position,testPosition,null));
            col--;
            testPosition.setCol(col);
        }
    }
}
