package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

        // PAWN
        if (piece.getPieceType() == PieceType.PAWN) {

            int prCntr = 1;
            while (prCntr < 5) {
                PieceType promoPiece; // this will iterate thru all piece types but king and pawn for promotions

                // white
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {

                    promoPiece = (i==7) ? PieceType.values()[prCntr] : null;

                    if (i == 2) {
                        ChessPosition frenchOpen = new ChessPosition(4, j);
                        ChessPosition newPosition = new ChessPosition(3, j);
                        if ((board.getPiece(frenchOpen) == null) && (board.getPiece(newPosition) == null)) {
                            possibleMoves.add(new ChessMove(myPosition, frenchOpen, null));
                        }
                    }

                    if (kInbounds(i + 1, j)) {
                        ChessPosition newPosition = new ChessPosition(i + 1, j);
                        if (board.getPiece(newPosition) == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPosition, promoPiece));
                        }
                    }

                    if (kInbounds(i + 1, j + 1)) {
                        ChessPosition potentialCaptureRight = new ChessPosition(i + 1, j + 1);

                        if (board.getPiece(potentialCaptureRight) != null) {
                            if (board.getPiece(potentialCaptureRight).getTeamColor() != piece.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, potentialCaptureRight, promoPiece));
                            }
                        }
                    }

                    if (kInbounds(i + 1, j - 1)) {
                        ChessPosition potentialCaptureLeft = new ChessPosition(i + 1, j - 1);
                        if (board.getPiece(potentialCaptureLeft) != null) {
                            if (board.getPiece(potentialCaptureLeft).getTeamColor() != piece.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, potentialCaptureLeft, promoPiece));
                            }
                        }
                    }

                    if (promoPiece == null) {
                        break;
                    }
                }

                // black
                else {

                    promoPiece = (i==2) ? PieceType.values()[prCntr] : null;

                    if (i == 7) {

                        ChessPosition frenchOpen = new ChessPosition(5, j);
                        ChessPosition newPosition = new ChessPosition(6, j);
                        if ((board.getPiece(frenchOpen) == null) && (board.getPiece(newPosition) == null)) {
                            possibleMoves.add(new ChessMove(myPosition, frenchOpen, null));
                        }
                    }

                    if (kInbounds(i - 1, j)) {
                        ChessPosition newPosition = new ChessPosition(i - 1, j);
                        if (board.getPiece(newPosition) == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPosition, promoPiece));
                        }
                    }

                    if (kInbounds(i - 1, j + 1)) {
                        ChessPosition potentialCaptureRight = new ChessPosition(i - 1, j + 1);

                        if (board.getPiece(potentialCaptureRight) != null) {
                            if (board.getPiece(potentialCaptureRight).getTeamColor() != piece.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, potentialCaptureRight, promoPiece));
                            }
                        }
                    }

                    if (kInbounds(i - 1, j - 1)) {
                        ChessPosition potentialCaptureLeft = new ChessPosition(i - 1, j - 1);
                        if (board.getPiece(potentialCaptureLeft) != null) {
                            if (board.getPiece(potentialCaptureLeft).getTeamColor() != piece.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, potentialCaptureLeft, promoPiece));
                            }
                        }
                    }

                    if (promoPiece == null) {
                        break;
                    }

                }

                prCntr++;

            }
        }

        // KING
        if (piece.getPieceType() == PieceType.KING) {

            // up
            i++;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // up-right
            j++;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // right
            i--;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // down-right
            i--;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // down
            j--;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // down-left
            j--;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // left
            i++;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

            // up-left
            i++;
            if (kInbounds(i,j)) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

        }

        // QUEEN
        if (piece.getPieceType() == PieceType.QUEEN) {
            // up and to the right
            while ((i < 8) && (j < 8)) {
                i++;
                j++;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else break;
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
                } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else break;
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
                } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else break;
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
                } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else break;
            }
            // reset to original position
            i = myPosition.getRow();
            j = myPosition.getColumn();

            // up
            while (i < 8) {
                i++;
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else break;
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
                } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else break;
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
                } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else break;
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
                } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                } else break;
            }

            return possibleMoves;
        }


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

    @Override
    public String toString() {

        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

}
