package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        // set up for white side
        int pwnCntr = 1;
        while (pwnCntr <= 8) {
            ChessPosition newPawnPosition = new ChessPosition(2, pwnCntr);
            ChessPiece newPawnPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(newPawnPosition, newPawnPiece);
            pwnCntr++;
        }

    }

    @Override
    public String toString() {
        List<String> boardPieces = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                boardPieces.add("|");
                if (squares[i][j] == null) {
                    boardPieces.add(" ");
                } else {
                    if (squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if (squares[i][j].getPieceType() == ChessPiece.PieceType.PAWN) {
                            boardPieces.add("P");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.KING) {
                            boardPieces.add("K");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.QUEEN) {
                            boardPieces.add("Q");
                        }
                        else boardPieces.add("X");
                    }
                    else boardPieces.add("X");
                }
            }
            boardPieces.add("\n");
        }



        return boardPieces.toString();
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

}
