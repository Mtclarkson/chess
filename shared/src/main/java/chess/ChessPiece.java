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
    public ChessMove inbounds(int i, int j, ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        if ((i <= 8) && (i >= 1) && (j <= 8) && (j >= 1)) {
            ChessPosition newPosition = new ChessPosition(i, j);
            ChessPiece targetPiece = board.getPiece(newPosition);
            if ((targetPiece == null) || (targetPiece.getTeamColor() != piece.getTeamColor())) {
                return (new ChessMove(myPosition, newPosition, null));
            }
        } return null;
    }

    private ChessMove pawnMove(int i, int j, ChessBoard board, ChessPiece piece, ChessPosition myPosition,
                               PieceType promoPiece, int attackVector) {
        int direction = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int newRow = i + direction;
        int newCol = j + attackVector;

        // bounds check
        if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
            return null;
        }

        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        ChessPiece targetPiece = board.getPiece(newPosition);

        if (attackVector == 0) {
            if (targetPiece == null) {
                return new ChessMove(myPosition, newPosition, promoPiece);
            }
        }
        else {
            if (targetPiece != null && targetPiece.getTeamColor() != piece.getTeamColor()) {
                return new ChessMove(myPosition, newPosition, promoPiece);
            }
        }
        return null;
    }

    private record MoveResult(ChessMove move, boolean keepGoing) {}

    private MoveResult checkIfOk(int i, int j, ChessBoard board,
                                 ChessPosition myPosition, ChessPiece piece) {
        ChessPosition newPosition = new ChessPosition(i, j);
        ChessPiece targetPiece = board.getPiece(newPosition);
        ChessMove move = new ChessMove(myPosition, newPosition, null);

        if (targetPiece == null) {
            return new MoveResult(move, true); // empty square, keep sliding
        } else if (targetPiece.getTeamColor() != piece.getTeamColor()) {
            return new MoveResult(move, false); // enemy, capture and stop
        } else {
            return new MoveResult(null, false); // friendly, stop, no move
        }
    }

    private List<ChessMove> kingFunctionality(int i, int j,
                                              ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        // up
        i++;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // up-right
        j++;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // right
        i--;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // down-right
        i--;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // down
        j--;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // down-left
        j--;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // left
        i++;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // up-left
        i++;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        return possibleMoves;
    }

    private List<ChessMove> bishopFunctionality(int i, int j,
                                                ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        while((i< 8)&&(j< 8)) {
            i++;
            j++;
            MoveResult result = checkIfOk(i, j, board, myPosition, piece);
            if (result.move() != null) {possibleMoves.add(result.move());}
            if (!result.keepGoing()) {break;}
        }
        // reset to original position
        i = myPosition.getRow();
        j = myPosition.getColumn();
        // down and to the right
        while((i > 1) && (j < 8)) {
            i--;
            j++;
            MoveResult result = checkIfOk(i, j, board, myPosition, piece);
            if (result.move() != null) {possibleMoves.add(result.move());}
            if (!result.keepGoing()) {break;}
        }
        i = myPosition.getRow();
        j = myPosition.getColumn();
        // down and to the left
        while((i > 1 ) && (j > 1)) {
            i--;
            j--;
            MoveResult result = checkIfOk(i, j, board, myPosition, piece);
            if (result.move() != null) {possibleMoves.add(result.move());}
            if (!result.keepGoing()) {break;}
        }
        i = myPosition.getRow();
        j = myPosition.getColumn();
        // up and to the left
        while((i < 8) && (j > 1)) {
            i++;
            j--;
            MoveResult result = checkIfOk(i, j, board, myPosition, piece);
            if (result.move() != null) {possibleMoves.add(result.move());}
            if (!result.keepGoing()) {break;}
        }
        return possibleMoves;
    }

    private List<ChessMove> rookFunctionality(int i, int j,
                                              ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        // up
        while (i < 8) {
            i++;
            MoveResult result = checkIfOk(i, j, board, myPosition, piece);
            if (result.move() != null) {possibleMoves.add(result.move());}
            if (!result.keepGoing()) {break;}
        }
        // reset to original position
        i = myPosition.getRow();
        // down
        while (i > 1) {
            i--;
            MoveResult result = checkIfOk(i, j, board, myPosition, piece);
            if (result.move() != null) {possibleMoves.add(result.move());}
            if (!result.keepGoing()) {break;}
        }
        i = myPosition.getRow();
        // to the right
        while (j < 8) {
            j++;
            MoveResult result = checkIfOk(i, j, board, myPosition, piece);
            if (result.move() != null) {possibleMoves.add(result.move());}
            if (!result.keepGoing()) {break;}
        }
        j = myPosition.getColumn();
        // to the left
        while (j > 1) {
            j--;
            MoveResult result = checkIfOk(i, j, board, myPosition, piece);
            if (result.move() != null) {possibleMoves.add(result.move());}
            if (!result.keepGoing()) {break;}
        }
        return possibleMoves;
    }

    private List<ChessMove> knightFunctionality(int i, int j,
                                                ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        // ^^>
        i+=2;
        j++;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // >>^
        i--;
        j++;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // >>v
        i-=2;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // vv>
        i--;
        j--;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // vv<
        j-=2;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // <<v
        i++;
        j--;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // <<^
        i+=2;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        // ^^<
        i++;
        j++;
        if (inbounds(i, j, board, piece, myPosition) != null) {
            possibleMoves.add(inbounds(i, j, board, piece, myPosition));
        }
        return possibleMoves;
    }

    private List<ChessMove> pawnFunctionality(int i, int j, ChessBoard board,
                                              ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        int direction = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int promotionRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 7 : 2;
        int startRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 2 : 7;
        boolean promotion = (i == promotionRow);
        PieceType[] promotionPieces = {
                PieceType.QUEEN,
                PieceType.BISHOP,
                PieceType.KNIGHT,
                PieceType.ROOK
        };

        if (!promotion) {
            // go forward
            ChessMove move = pawnMove(i, j, board, piece, myPosition, null, 0);
            if (move != null) {
                possibleMoves.add(move);
            }
            // double forward move
            if (i == startRow) {
                ChessPosition oneForward = new ChessPosition(i + direction, j);
                ChessPosition twoForward = new ChessPosition(i + 2 * direction, j);
                if (board.getPiece(oneForward) == null && board.getPiece(twoForward) == null) {
                    possibleMoves.add(
                            new ChessMove(myPosition, twoForward, null)
                    );
                }
            }
            // capture right
            move = pawnMove(i, j, board, piece, myPosition, null, 1);
            if (move != null) {
                possibleMoves.add(move);
            }
            // capture left
            move = pawnMove(i, j, board, piece, myPosition, null, -1);
            if (move != null) {
                possibleMoves.add(move);
            }
        }

        else {
            for (PieceType promoPiece : promotionPieces) {
                // forward promotion
                ChessMove move = pawnMove(i, j, board, piece, myPosition, promoPiece, 0);
                if (move != null) {
                    possibleMoves.add(move);
                }
                // capture-right promotion
                move = pawnMove(i, j, board, piece, myPosition, promoPiece, 1);
                if (move != null) {
                    possibleMoves.add(move);
                }
                // capture-left promotion
                move = pawnMove(i, j, board, piece, myPosition, promoPiece, -1);
                if (move != null) {
                    possibleMoves.add(move);
                }
            }
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
