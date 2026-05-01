package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return type;
    }

    /**
     * helper funtion for knight
     * @return boolean for whether a knight's move is in bounds
     */
    public boolean kInbounds(int i, int j) {
        return (i <= 8) && (i >= 1) && (j <= 8) && (j >= 1);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        int i = myPosition.getRow();
        int j = myPosition.getColumn();

        // BISHOP
        if (piece.getPieceType() == PieceType.BISHOP) {
            // up and to the right
            while ((i < 8) && (j < 8)) {
                i++;
                j++;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else break;
            }
            // reset to original position
            i = myPosition.getRow();
            j = myPosition.getColumn();

            // down and to the right
            while ((i > 1) && (j < 8)) {
                i--;
                j++;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else break;
            }
            // reset to original position
            i = myPosition.getRow();
            j = myPosition.getColumn();

            // down and to the left
            while ((i > 1) && (j > 1)) {
                i--;
                j--;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else break;
            }
            // reset to original position
            i = myPosition.getRow();
            j = myPosition.getColumn();

            // up and to the left
            while ((i < 8) && (j > 1)) {
                i++;
                j--;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else break;
            }
        }

        // KNIGHT
        if (piece.getPieceType() == PieceType.KNIGHT) {

            // ^^>
            i+=2;
            j++;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // >>^
            i--;
            j++;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // >>v
            i-=2;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // vv>
            i--;
            j--;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
            // vv<
            j-=2;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // <<v
            i++;
            j--;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // <<^
            i+=2;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // ^^<
            i++;
            j++;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

        }

        // ROOK
        else if (piece.getPieceType() == PieceType.ROOK) {
            // up
            while (i < 8) {
                i++;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else break;
            }
            // reset to original position
            i = myPosition.getRow();

            // down
            while (i > 1) {
                i--;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else break;
            }
            // reset to original position
            i = myPosition.getRow();

            // to the right
            while (j < 8) {
                j++;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else break;
            }
            // reset to original position
            j = myPosition.getColumn();

            // to the left
            while (j > 1) {
                j--;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
                else break;
            }
        }
        return possibleMoves;
    }
}
