package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
    }

    ChessBoard gameboard = new ChessBoard();
    TeamColor currentTeam;

    // find a way to get the king position as a global variable

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = gameboard.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        // list of possible moves
        ArrayList<ChessMove> moves = new ArrayList<>(piece.pieceMoves(gameboard, startPosition));

        for (ChessMove move : moves) {
            ChessPosition endPosition = move.getEndPosition();
            ChessPiece targetPiece = gameboard.getPiece(endPosition);
            // make the move
            gameboard.addPiece(endPosition,piece);
            gameboard.addPiece(startPosition, null);
            // see if that move put you in check
            if (!this.isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }
            // move back, silly
            gameboard.addPiece(endPosition,targetPiece);
            gameboard.addPiece(startPosition, piece);
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = gameboard.getPiece(startPosition);
        if (this.validMoves(startPosition).contains(move)) {
            gameboard.addPiece(endPosition,piece);
            gameboard.addPiece(startPosition, null);
        } else {
            throw new InvalidMoveException("Invalid move!");
        }
    }

    public ChessPosition getKingPosition(TeamColor teamColor) {
        // get teamColor's King Position
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = gameboard.getPiece(new ChessPosition(i,j));
                if (piece != null) {
                    if ((piece.getPieceType() == ChessPiece.PieceType.KING) && (piece.getTeamColor() == teamColor)) {
                        return new ChessPosition(i, j);
                    }
                }
            }
        }

        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition kingPosition = getKingPosition(teamColor);

        // get opposite team's pieceMoves lists; if any have an endPosition at teamColor's King position, teamColor is in Check;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition piecePosition = new ChessPosition(i,j);
                ChessPiece piece = gameboard.getPiece(piecePosition);
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        Collection<ChessMove> enemyMoves = piece.pieceMoves(gameboard, piecePosition);
                        ArrayList<ChessMove> enemyMovesList = new ArrayList<>(enemyMoves);
                        for (ChessMove chessMove : enemyMovesList) {
                            ChessPosition enemyPosition = chessMove.getEndPosition();
                            if (enemyPosition.equals(kingPosition)) {
                                return true; // check found
                            }
                        }
                    }
                }
            }
        }

        // no check was found
        return false;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        if (this.isInCheck(teamColor)) { // if the king is currently in check
            return this.validMoves(kingPosition).isEmpty(); // moves where king isn't in check is empty
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameboard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return currentTeam == chessGame.currentTeam && Objects.equals(gameboard, chessGame.gameboard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeam, gameboard);
    }
}
