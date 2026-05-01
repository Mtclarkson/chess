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
            // reset to original position
            i = myPosition.getRow();
            j = myPosition.getColumn();
        }
        return possibleMoves;
    }
}
