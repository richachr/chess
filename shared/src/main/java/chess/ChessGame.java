package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor turnColor;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turnColor = TeamColor.WHITE;
        TeamColor.WHITE.setKingPosition(new ChessPosition(1,5));
        TeamColor.BLACK.setKingPosition(new ChessPosition(8,5));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(getBoard(), chessGame.getBoard()) && turnColor == chessGame.turnColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), turnColor);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE, BLACK;
        private TeamColor opponent;
        private ChessPosition kingPosition;

        static {
            WHITE.opponent = BLACK;
            BLACK.opponent = WHITE;
        }

        public TeamColor getOpponent() {
            return opponent;
        }

        public ChessPosition getKingPosition() {
            return kingPosition;
        }

        public void setKingPosition(ChessPosition kingPosition) {
            this.kingPosition = kingPosition;
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    private Collection<ChessPosition> teamValidMovePositions(TeamColor color) {
        ChessPosition testPosition = new ChessPosition(1,1);
        ChessPiece testPiece;
        ArrayList<ChessPosition> opposingTeamMoves = new ArrayList<>();
        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                testPosition.setRow(row);
                testPosition.setCol(col);
                testPiece = board.getPiece(testPosition);
                if(testPiece != null && testPiece.getTeamColor() != color) {
                    var validMoves = validMoves(testPosition);
                    for(ChessMove move : validMoves) {
                        opposingTeamMoves.add(move.getEndPosition());
                    }
                }
            }
        }
        return opposingTeamMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPosition> opposingTeamValidMoves = teamValidMovePositions(teamColor);
        return opposingTeamValidMoves.contains(teamColor.getKingPosition());
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
