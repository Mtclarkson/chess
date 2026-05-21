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
     * helper function for checking bounds
     *
     * @return boolean for whether a knight's move is in bounds
     */
    public boolean inbounds(int i, int j) {
        return (i <= 8) && (i >= 1) && (j <= 8) && (j >= 1);
    }

    private boolean breakFlag = false;

    private ChessMove checkIfOk(int i, int j, ChessBoard board,
                                ChessPosition myPosition, ChessPiece piece) {
        ChessPosition newPosition = new ChessPosition(i, j);
        ChessPiece targetPiece = board.getPiece(newPosition);
        if (targetPiece == null) {
            return new ChessMove(myPosition, newPosition, null);
        } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
            breakFlag = true;
            return new ChessMove(myPosition, newPosition, null);
        } else {
            breakFlag = true;
            return null;
        }
    }

    private List<ChessMove> kingFunctionality(int i, int j,
                                              ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        // up
        i++;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // up-right
        j++;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // right
        i--;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // down-right
        i--;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // down
        j--;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // down-left
        j--;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // left
        i++;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // up-left
        i++;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        return possibleMoves;
    }

    private List<ChessMove> bishopFunctionality(int i, int j,
                                                ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        while((i< 8)&&(j< 8)) {
            i++;
            j++;
            ChessMove okMove = checkIfOk(i, j, board, myPosition, piece);
            if (okMove != null) {
                possibleMoves.add(okMove);
            }
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
        // reset to original position
        i = myPosition.getRow();
        j = myPosition.getColumn();
        // down and to the right
        while((i > 1) && (j < 8)) {
            i--;
            j++;
            ChessMove okMove = checkIfOk(i, j, board, myPosition, piece);
            if (okMove != null) {
                possibleMoves.add(okMove);
            }
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
        i = myPosition.getRow();
        j = myPosition.getColumn();
        // down and to the left
        while((i > 1 ) && (j > 1)) {
            i--;
            j--;
            ChessMove okMove = checkIfOk(i, j, board, myPosition, piece);
            if (okMove != null) {
                possibleMoves.add(okMove);
            }
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
        i =myPosition.getRow();
        j =myPosition.getColumn();
        // up and to the left
        while((i < 8) && (j > 1)) {
            i++;
            j--;
            ChessMove okMove = checkIfOk(i, j, board, myPosition, piece);
            if (okMove != null) {
                possibleMoves.add(okMove);
            }
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
        return possibleMoves;
    }

    private List<ChessMove> rookFunctionality(int i, int j,
                                              ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        // up
        while (i < 8) {
            i++;
            ChessMove okMove = checkIfOk(i, j, board, myPosition, piece);
            if (okMove != null) {possibleMoves.add(okMove);}
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
        // reset to original position
        i = myPosition.getRow();
        // down
        while (i > 1) {
            i--;
            ChessMove okMove = checkIfOk(i, j, board, myPosition, piece);
            if (okMove != null) {possibleMoves.add(okMove);}
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
        i = myPosition.getRow();
        // to the right
        while (j < 8) {
            j++;
            ChessMove okMove = checkIfOk(i, j, board, myPosition, piece);
            if (okMove != null) {possibleMoves.add(okMove);}
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
        j = myPosition.getColumn();
        // to the left
        while (j > 1) {
            j--;
            ChessMove okMove = checkIfOk(i, j, board, myPosition, piece);
            if (okMove != null) {possibleMoves.add(okMove);}
            if (breakFlag) {
                breakFlag = false;
                break;
            }
        }
        return possibleMoves;
    }

    private List<ChessMove> knightFunctionality(int i, int j,
                                                ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        // ^^>
        i+=2;
        j++;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // >>^
        i--;
        j++;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // >>v
        i-=2;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // vv>
        i--;
        j--;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // vv<
        j-=2;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // <<v
        i++;
        j--;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // <<^
        i+=2;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        // ^^<
        i++;
        j++;
        if (inbounds(i,j)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        return possibleMoves;
    }

    private List<ChessMove> pawnFunctionality(int i, int j,
                                              ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        int prCntr = 1;
        while (prCntr < 5) {
            PieceType promoPiece; // this will iterate through all piece types but king and pawn for promotions
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
                if (inbounds(i + 1, j)) {
                    ChessPosition newPosition = new ChessPosition(i + 1, j);
                    if (board.getPiece(newPosition) == null) {
                        possibleMoves.add(new ChessMove(myPosition, newPosition, promoPiece));
                    }
                }
                if (inbounds(i + 1, j + 1)) {
                    ChessPosition potentialCaptureRight = new ChessPosition(i + 1, j + 1);

                    if (board.getPiece(potentialCaptureRight) != null) {
                        if (board.getPiece(potentialCaptureRight).getTeamColor() != piece.getTeamColor()) {
                            possibleMoves.add(new ChessMove(myPosition, potentialCaptureRight, promoPiece));
                        }
                    }
                }
                if (inbounds(i + 1, j - 1)) {
                    ChessPosition potentialCaptureLeft = new ChessPosition(i + 1, j - 1);
                    if (board.getPiece(potentialCaptureLeft) != null) {
                        if (board.getPiece(potentialCaptureLeft).getTeamColor() != piece.getTeamColor()) {
                            possibleMoves.add(new ChessMove(myPosition, potentialCaptureLeft, promoPiece));
                        }
                    }
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
                if (inbounds(i - 1, j)) {
                    ChessPosition newPosition = new ChessPosition(i - 1, j);
                    if (board.getPiece(newPosition) == null) {
                        possibleMoves.add(new ChessMove(myPosition, newPosition, promoPiece));
                    }
                }
                if (inbounds(i - 1, j + 1)) {
                    ChessPosition potentialCaptureRight = new ChessPosition(i - 1, j + 1);
                    if (board.getPiece(potentialCaptureRight) != null) {
                        if (board.getPiece(potentialCaptureRight).getTeamColor() != piece.getTeamColor()) {
                            possibleMoves.add(new ChessMove(myPosition, potentialCaptureRight, promoPiece));
                        }
                    }
                }
                if (inbounds(i - 1, j - 1)) {
                    ChessPosition potentialCaptureLeft = new ChessPosition(i - 1, j - 1);
                    if (board.getPiece(potentialCaptureLeft) != null) {
                        if (board.getPiece(potentialCaptureLeft).getTeamColor() != piece.getTeamColor()) {
                            possibleMoves.add(new ChessMove(myPosition, potentialCaptureLeft, promoPiece));
                        }
                    }
                }
            }
            if (promoPiece == null) {
                break;
            }
            prCntr++;
        }
        return possibleMoves;
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
            possibleMoves.addAll(pawnFunctionality(i, j, board, myPosition, piece));
        }
        // KING
        if (piece.getPieceType() == PieceType.KING) {
            possibleMoves.addAll(kingFunctionality(i, j, board, myPosition, piece));
        }
        // QUEEN
        if (piece.getPieceType() == PieceType.QUEEN) {
            possibleMoves.addAll(bishopFunctionality(i, j, board, myPosition, piece));
            possibleMoves.addAll(rookFunctionality(i, j, board, myPosition, piece));
        }
        // BISHOP
        if (piece.getPieceType() == PieceType.BISHOP) {
            possibleMoves.addAll(bishopFunctionality(i, j, board, myPosition, piece));
        }
        // KNIGHT
        if (piece.getPieceType() == PieceType.KNIGHT) {
            possibleMoves.addAll(knightFunctionality(i, j, board, myPosition, piece));
        }
        // ROOK
        else if (piece.getPieceType() == PieceType.ROOK) {
            possibleMoves.addAll(rookFunctionality(i, j, board, myPosition, piece));
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
