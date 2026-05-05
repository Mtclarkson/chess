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
        ChessPosition LeftWhiteRookPosition = new ChessPosition(1,1);
        ChessPosition LeftWhiteKnightPosition = new ChessPosition(1,2);
        ChessPosition LeftWhiteBishopPosition = new ChessPosition(1,3);
        ChessPosition WhiteKingPosition = new ChessPosition(1,5);
        ChessPosition WhiteQueenPosition = new ChessPosition(1,4);
        ChessPosition RightWhiteBishopPosition = new ChessPosition(1,6);
        ChessPosition RightWhiteKnightPosition = new ChessPosition(1,7);
        ChessPosition RightWhiteRookPosition = new ChessPosition(1,8);

        ChessPiece newWhiteRookPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece newWhiteKnightPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece newWhiteBishopPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece newWhiteKingPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece newWhiteQueenPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);

        addPiece(LeftWhiteRookPosition, newWhiteRookPiece);
        addPiece(LeftWhiteKnightPosition, newWhiteKnightPiece);
        addPiece(LeftWhiteBishopPosition, newWhiteBishopPiece);
        addPiece(WhiteKingPosition, newWhiteKingPiece);
        addPiece(WhiteQueenPosition, newWhiteQueenPiece);
        addPiece(RightWhiteRookPosition, newWhiteRookPiece);
        addPiece(RightWhiteKnightPosition, newWhiteKnightPiece);
        addPiece(RightWhiteBishopPosition, newWhiteBishopPiece);

        // black side back row
        ChessPosition LeftBlackRookPosition = new ChessPosition(8,1);
        ChessPosition LeftBlackKnightPosition = new ChessPosition(8,2);
        ChessPosition LeftBlackBishopPosition = new ChessPosition(8,3);
        ChessPosition BlackKingPosition = new ChessPosition(8,5);
        ChessPosition BlackQueenPosition = new ChessPosition(8,4);
        ChessPosition RightBlackBishopPosition = new ChessPosition(8,6);
        ChessPosition RightBlackKnightPosition = new ChessPosition(8,7);
        ChessPosition RightBlackRookPosition = new ChessPosition(8,8);

        ChessPiece newBlackRookPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece newBlackKnightPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece newBlackBishopPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece newBlackKingPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece newBlackQueenPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);

        addPiece(LeftBlackRookPosition, newBlackRookPiece);
        addPiece(LeftBlackKnightPosition, newBlackKnightPiece);
        addPiece(LeftBlackBishopPosition, newBlackBishopPiece);
        addPiece(BlackKingPosition, newBlackKingPiece);
        addPiece(BlackQueenPosition, newBlackQueenPiece);
        addPiece(RightBlackRookPosition, newBlackRookPiece);
        addPiece(RightBlackKnightPosition, newBlackKnightPiece);
        addPiece(RightBlackBishopPosition, newBlackBishopPiece);

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
