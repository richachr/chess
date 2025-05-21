package chess;

public class PawnMovesGenerator extends MovesGenerator {
    public PawnMovesGenerator(ChessBoard board, ChessPosition position) {
        super(board, position);
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition testPosition;
        if(ownColor == ChessGame.TeamColor.BLACK) {
            testPosition = new ChessPosition(row-1,col);
            if(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
                doubleMoveTestAndAdd(row, col);
                checkPromotionAndAdd(position,testPosition);
            }
            testPosition = new ChessPosition(row-1,col+1);
            if(testPosition.isInBounds() && board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != ownColor) {
                checkPromotionAndAdd(position,testPosition);
            }
            testPosition = new ChessPosition(row-1,col-1);
            if(testPosition.isInBounds() && board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != ownColor) {
                checkPromotionAndAdd(position,testPosition);
            }
        }
        // White pawns
        else {
            testPosition = new ChessPosition(row+1,col);
            if(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
                doubleMoveTestAndAdd(row, col);
                checkPromotionAndAdd(position,testPosition);
            }
            testPosition = new ChessPosition(row+1,col+1);
            if(testPosition.isInBounds() && board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != ownColor) {
                checkPromotionAndAdd(position,testPosition);
            }
            testPosition = new ChessPosition(row+1,col-1);
            if(testPosition.isInBounds() && board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != ownColor) {
                checkPromotionAndAdd(position, testPosition);
            }
        }
    }

    private boolean isPromotable(int row, ChessGame.TeamColor color) {
        return ((color == ChessGame.TeamColor.BLACK && row == 1) || (color == chess.ChessGame.TeamColor.WHITE && row == 8));
    }

    private void checkPromotionAndAdd(ChessPosition originalPosition, ChessPosition testPosition) {
        if(isPromotable(testPosition.getRow(),ownColor)) {
            validMoves.add(new ChessMove(originalPosition,testPosition, ChessPiece.PieceType.ROOK));
            validMoves.add(new ChessMove(originalPosition,testPosition, ChessPiece.PieceType.KNIGHT));
            validMoves.add(new ChessMove(originalPosition,testPosition, ChessPiece.PieceType.BISHOP));
            validMoves.add(new ChessMove(originalPosition,testPosition, ChessPiece.PieceType.QUEEN));
        } else {
            validMoves.add(new ChessMove(originalPosition,testPosition,null));
        }
    }

    private void doubleMoveTestAndAdd(int row, int col) {
        if(ownColor == ChessGame.TeamColor.BLACK && row == 7) {
            ChessPosition doubleTestPosition = new ChessPosition(row-2,col);
            if(board.getPiece(doubleTestPosition) == null) {
                validMoves.add(new ChessMove(position,doubleTestPosition,null));
            }
        } else if(ownColor == ChessGame.TeamColor.WHITE && row == 2) {
            ChessPosition doubleTestPosition = new ChessPosition(row+2,col);
            if(board.getPiece(doubleTestPosition) == null) {
                validMoves.add(new ChessMove(position,doubleTestPosition,null));
            }
        }
    }
}
