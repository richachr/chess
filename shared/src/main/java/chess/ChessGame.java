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
public class ChessGame implements Cloneable {
    ChessBoard board;
    TeamColor turnColor;
    boolean isTestGame;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turnColor = TeamColor.WHITE;
        TeamColor.WHITE.setKingPosition(new ChessPosition(1,5));
        TeamColor.BLACK.setKingPosition(new ChessPosition(8,5));
        this.isTestGame = false;
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessGame newClone = (ChessGame) super.clone();
        newClone.setBoard((ChessBoard) board.clone());
        return newClone;
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
        if(board.getPiece(startPosition) == null) {return null;}
        ArrayList<ChessMove> possibleMoves = new ArrayList<>(board.getPiece(startPosition).pieceMoves(board,startPosition));
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        for(ChessMove move : possibleMoves) {
            try {
                var preMoveBoard = this.board;
                ChessGame testGame = (ChessGame) this.clone();
                testGame.isTestGame = true;
                testGame.makeMove(move);
                assert preMoveBoard == this.board;
                if(!testGame.simpleIsInCheck(this.board.getPiece(startPosition).getTeamColor())) {
                    validMoves.add(move);
                }
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            } catch (InvalidMoveException e) {
                System.out.println(e.getLocalizedMessage());
                validMoves.remove(move);
            }
        }
        return validMoves;
    }

    private Collection<ChessPosition> teamValidMovePositions(TeamColor color, boolean simpleMode) {
        ChessPosition testPosition = new ChessPosition(1,1);
        ChessPiece testPiece;
        ArrayList<ChessPosition> teamMoves = new ArrayList<>();
        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                testPosition.setRow(row);
                testPosition.setCol(col);
                testPiece = board.getPiece(testPosition);
                if(testPiece != null && testPiece.getTeamColor() == color) {
                    Collection<ChessMove> validMoves;
                    if(color == turnColor && !simpleMode) {
                        validMoves = this.validMoves(testPosition);
                    } else {
                        validMoves = testPiece.pieceMoves(board,testPosition);
                    }
                    for(ChessMove move : validMoves) {
                        teamMoves.add(move.getEndPosition());
                    }
                }
            }
        }
        return teamMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        if(pieceToMove == null) {throw new InvalidMoveException("Move played on an empty space.");}
        TeamColor pieceColor = pieceToMove.getTeamColor();
        if(!isTestGame && pieceColor != turnColor) {
            throw new InvalidMoveException("Move played out of turn.");
        }
        else if (!(pieceToMove.pieceMoves(board,move.getStartPosition()).contains(move))) {
            throw new InvalidMoveException("Not a valid move by chess piece rules.");
        }
        if(move.getPromotionPiece() != null) {
            pieceToMove = new ChessPiece(pieceToMove.getTeamColor(),move.getPromotionPiece());
        }
        board.addPiece(move.getEndPosition(),pieceToMove);
        board.addPiece(move.getStartPosition(),null);
        if(isInCheck(pieceColor)) {
            throw new InvalidMoveException("This move would place your team in check.");
        }
        this.turnColor = turnColor.getOpponent();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPosition> opposingTeamValidMoves = teamValidMovePositions(teamColor.opponent,false);
        findSetKingPosition();
        return opposingTeamValidMoves.contains(teamColor.getKingPosition());
    }

    public boolean simpleIsInCheck(TeamColor teamColor) {
        Collection<ChessPosition> opposingTeamValidMoves = teamValidMovePositions(teamColor.opponent,true);
        return opposingTeamValidMoves.contains(teamColor.getKingPosition());
    }

    private void findSetKingPosition() {
        ChessPosition testPosition = new ChessPosition(1,1);
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                testPosition.setRow(i);
                testPosition.setCol(j);
                ChessPiece testPiece = board.getPiece(testPosition);
                if(testPiece != null && testPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    testPiece.getTeamColor().setKingPosition(new ChessPosition(i,j));
                }
            }
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && teamValidMovePositions(teamColor,false).isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && teamValidMovePositions(teamColor,false).isEmpty();
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
