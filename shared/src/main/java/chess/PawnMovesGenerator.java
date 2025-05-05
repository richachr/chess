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
                if(row == 7) {
                    ChessPosition doubleTestPosition = new ChessPosition(row-2,col);
                    if(board.getPiece(doubleTestPosition) == null) {
                        validMoves.add(new ChessMove(position,doubleTestPosition,null));
                    }
                }
                if(isPromotable(testPosition.getRow(),ownColor)) {
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.QUEEN));
                } else {
                    validMoves.add(new ChessMove(position,testPosition,null));
                }
            }
            testPosition = new ChessPosition(row-1,col+1);
            if(testPosition.isInBounds() && board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != ownColor) {
                if(isPromotable(testPosition.getRow(),ownColor)) {
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.QUEEN));
                } else {
                    validMoves.add(new ChessMove(position,testPosition,null));
                }
            }
            testPosition = new ChessPosition(row-1,col-1);
            if(testPosition.isInBounds() && board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != ownColor) {
                if(isPromotable(testPosition.getRow(),ownColor)) {
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.QUEEN));
                } else {
                    validMoves.add(new ChessMove(position,testPosition,null));
                }
            }
        }
        // White pawns
        else {
            testPosition = new ChessPosition(row+1,col);
            if(testPosition.isInBounds() && board.getPiece(testPosition) == null) {
                if(row == 2) {
                    ChessPosition doubleTestPosition = new ChessPosition(row+2,col);
                    if(board.getPiece(doubleTestPosition) == null) {
                        validMoves.add(new ChessMove(position,doubleTestPosition,null));
                    }
                }
                if(isPromotable(testPosition.getRow(),ownColor)) {
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.QUEEN));
                } else {
                    validMoves.add(new ChessMove(position,testPosition,null));
                }
            }
            testPosition = new ChessPosition(row+1,col+1);
            if(testPosition.isInBounds() && board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != ownColor) {
                if(isPromotable(testPosition.getRow(),ownColor)) {
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.QUEEN));
                } else {
                    validMoves.add(new ChessMove(position,testPosition,null));
                }
            }
            testPosition = new ChessPosition(row+1,col-1);
            if(testPosition.isInBounds() && board.getPiece(testPosition) != null && board.getPiece(testPosition).getTeamColor() != ownColor) {
                if(isPromotable(testPosition.getRow(),ownColor)) {
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position,testPosition, ChessPiece.PieceType.QUEEN));
                } else {
                    validMoves.add(new ChessMove(position,testPosition,null));
                }
            }
        }
    }

    private boolean isPromotable(int row, ChessGame.TeamColor color) {
        return ((color == ChessGame.TeamColor.BLACK && row == 1) || (color == chess.ChessGame.TeamColor.WHITE && row == 8));
    }
}
