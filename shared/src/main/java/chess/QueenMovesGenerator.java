package chess;

public class QueenMovesGenerator extends MovesGenerator {
    public QueenMovesGenerator(ChessBoard board, ChessPosition position) {
        super(board, position);
        validMoves.addAll(new BishopMovesGenerator(board,position).getValidMoves());
        validMoves.addAll(new RookMovesGenerator(board,position).getValidMoves());
    }
}
