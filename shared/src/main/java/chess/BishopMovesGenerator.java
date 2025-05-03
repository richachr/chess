package chess;

public class BishopMovesGenerator extends MovesGenerator {
    public BishopMovesGenerator(ChessBoard board, ChessPosition position) {
        super(board, position);
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition testPosition = new ChessPosition(position.getRow(), position.getColumn());
        // Check top right
        row++;
        col++;
        testPosition.setRow(row);
        testPosition.setCol(col);
        while(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
            validMoves.add(new ChessMove(position, new ChessPosition(row,col),null));
            row++;
            col++;
            testPosition.setRow(row);
            testPosition.setCol(col);
        }
        if(testPosition.isInBounds() && board.getPiece(testPosition).getTeamColor() != ownColor) {
            validMoves.add(new ChessMove(position,new ChessPosition(row,col),null));
        }
        // Check top left
        row = position.getRow() + 1;
        col = position.getColumn() - 1;
        testPosition.setRow(row);
        testPosition.setCol(col);
        while(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
            validMoves.add(new ChessMove(position,new ChessPosition(row,col),null));
            row++;
            col--;
            testPosition.setRow(row);
            testPosition.setCol(col);
        }
        if(testPosition.isInBounds() && board.getPiece(testPosition).getTeamColor() != ownColor) {
            validMoves.add(new ChessMove(position,new ChessPosition(row,col),null));
        }
        // Check bottom left
        row = position.getRow() - 1;
        col = position.getColumn() - 1;
        testPosition.setRow(row);
        testPosition.setCol(col);
        while(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
            validMoves.add(new ChessMove(position,new ChessPosition(row,col),null));
            row--;
            col--;
            testPosition.setRow(row);
            testPosition.setCol(col);
        }
        if(testPosition.isInBounds() && board.getPiece(testPosition).getTeamColor() != ownColor) {
            validMoves.add(new ChessMove(position,new ChessPosition(row,col),null));
        }
        // Check bottom right
        row = position.getRow() - 1;
        col = position.getColumn() + 1;
        testPosition.setRow(row);
        testPosition.setCol(col);
        while(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
            validMoves.add(new ChessMove(position,new ChessPosition(row,col),null));
            row--;
            col++;
            testPosition.setRow(row);
            testPosition.setCol(col);
        }
        if(testPosition.isInBounds() && board.getPiece(testPosition).getTeamColor() != ownColor) {
            validMoves.add(new ChessMove(position,new ChessPosition(row,col),null));
        }
    }
}
