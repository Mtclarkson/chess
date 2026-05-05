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

    }

    @Override
    public String toString() {
        Deque<String> boardPieces = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardPieces.push("|");
                if (squares[i][j] == null) {
                    boardPieces.push(" ");
                } else {
                    if (squares[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if (squares[i][j].getPieceType() == ChessPiece.PieceType.PAWN) {
                            boardPieces.push("P");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.KING) {
                            boardPieces.push("K");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.QUEEN) {
                            boardPieces.push("Q");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.BISHOP) {
                            boardPieces.push("B");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.KNIGHT) {
                            boardPieces.push("N");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.ROOK) {
                            boardPieces.push("R");
                        }
                    }
                    if (squares[i][j].getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if (squares[i][j].getPieceType() == ChessPiece.PieceType.PAWN) {
                            boardPieces.push("p");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.KING) {
                            boardPieces.push("k");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.QUEEN) {
                            boardPieces.push("q");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.BISHOP) {
                            boardPieces.push("b");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.KNIGHT) {
                            boardPieces.push("n");
                        }
                        else if (squares[i][j].getPieceType() == ChessPiece.PieceType.ROOK) {
                            boardPieces.push("r");
                        }
                    }
                }
            }
            boardPieces.push("\n");
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
