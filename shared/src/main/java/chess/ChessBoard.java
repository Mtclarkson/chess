package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    // initialize board, an 8x8 array of ChessPiece objects.
    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // set pawns for both sides
        int pwnCntr = 1;
        while (pwnCntr <= 8) {
            ChessPosition newWhitePawnPosition = new ChessPosition(2, pwnCntr);
            ChessPiece newWhitePawnPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(newWhitePawnPosition, newWhitePawnPiece);

            ChessPosition newBlackPawnPosition = new ChessPosition(7, pwnCntr);
            ChessPiece newBlackPawnPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(newBlackPawnPosition, newBlackPawnPiece);

            pwnCntr++;
        }

        // white side back row
        ChessPosition leftWhiteRookPosition = new ChessPosition(1,1);
        ChessPosition leftWhiteKnightPosition = new ChessPosition(1,2);
        ChessPosition leftWhiteBishopPosition = new ChessPosition(1,3);
        ChessPosition whiteKingPosition = new ChessPosition(1,5);
        ChessPosition whiteQueenPosition = new ChessPosition(1,4);
        ChessPosition rightWhiteBishopPosition = new ChessPosition(1,6);
        ChessPosition rightWhiteKnightPosition = new ChessPosition(1,7);
        ChessPosition rightWhiteRookPosition = new ChessPosition(1,8);

        ChessPiece newWhiteRookPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece newWhiteKnightPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece newWhiteBishopPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece newWhiteKingPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece newWhiteQueenPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);

        addPiece(leftWhiteRookPosition, newWhiteRookPiece);
        addPiece(leftWhiteKnightPosition, newWhiteKnightPiece);
        addPiece(leftWhiteBishopPosition, newWhiteBishopPiece);
        addPiece(whiteKingPosition, newWhiteKingPiece);
        addPiece(whiteQueenPosition, newWhiteQueenPiece);
        addPiece(rightWhiteRookPosition, newWhiteRookPiece);
        addPiece(rightWhiteKnightPosition, newWhiteKnightPiece);
        addPiece(rightWhiteBishopPosition, newWhiteBishopPiece);

        // black side back row
        ChessPosition leftBlackRookPosition = new ChessPosition(8,1);
        ChessPosition leftBlackKnightPosition = new ChessPosition(8,2);
        ChessPosition leftBlackBishopPosition = new ChessPosition(8,3);
        ChessPosition blackKingPosition = new ChessPosition(8,5);
        ChessPosition blackQueenPosition = new ChessPosition(8,4);
        ChessPosition rightBlackBishopPosition = new ChessPosition(8,6);
        ChessPosition rightBlackKnightPosition = new ChessPosition(8,7);
        ChessPosition rightBlackRookPosition = new ChessPosition(8,8);

        ChessPiece newBlackRookPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece newBlackKnightPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece newBlackBishopPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece newBlackKingPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece newBlackQueenPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);

        addPiece(leftBlackRookPosition, newBlackRookPiece);
        addPiece(leftBlackKnightPosition, newBlackKnightPiece);
        addPiece(leftBlackBishopPosition, newBlackBishopPiece);
        addPiece(blackKingPosition, newBlackKingPiece);
        addPiece(blackQueenPosition, newBlackQueenPiece);
        addPiece(rightBlackRookPosition, newBlackRookPiece);
        addPiece(rightBlackKnightPosition, newBlackKnightPiece);
        addPiece(rightBlackBishopPosition, newBlackBishopPiece);

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        for (int row = 7; row >= 0; row--) {
            sb.append(row + 1).append(" ");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                sb.append(piece == null ? "." : pieceToChar(piece)).append(" ");
            }
            sb.append(row + 1).append("\n");
        }
        sb.append("  a b c d e f g h");
        return sb.toString();
    }

    private char pieceToChar(ChessPiece piece) {
        char c = switch (piece.getPieceType()) {
            case KING   -> 'k';
            case QUEEN  -> 'q';
            case ROOK   -> 'r';
            case BISHOP -> 'b';
            case KNIGHT -> 'n';
            case PAWN   -> 'p';
        };
        return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? Character.toUpperCase(c) : c;
    }
}
