package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType pieceType;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public String toString() {
        if(pieceColor== ChessGame.TeamColor.WHITE) {
            switch (pieceType) {
                case KING -> {return "K";}
                case QUEEN -> {return "Q";}
                case BISHOP -> {return "B";}
                case KNIGHT -> {return "N";}
                case ROOK -> {return "R";}
                case PAWN -> {return "P";}
            }
        } else {
            switch (pieceType) {
                case KING -> {return "k";}
                case QUEEN -> {return "q";}
                case BISHOP -> {return "b";}
                case KNIGHT -> {return "n";}
                case ROOK -> {return "r";}
                case PAWN -> {return "p";}
            }
        }
        return "ChessPiece";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch(pieceType) {
            case KING -> {return new KingMovesGenerator(board,myPosition).getValidMoves();}
            case KNIGHT -> {return new KnightMovesGenerator(board,myPosition).getValidMoves();}
            case ROOK -> {return new RookMovesGenerator(board,myPosition).getValidMoves();}
            case BISHOP -> {return new BishopMovesGenerator(board,myPosition).getValidMoves();}
            case QUEEN -> {return new QueenMovesGenerator(board,myPosition).getValidMoves();}
            case PAWN -> {return new PawnMovesGenerator(board,myPosition).getValidMoves();}
            default -> throw new IllegalStateException("Unexpected value: " + pieceType);
        }
    }
}
