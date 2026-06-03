package client;


import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayClient {
    private final ServerFacade server;
    private final String authToken;
    private final String playerColor;
    private final GameData gameData;

    public GameplayClient(String serverUrl, String authToken, GameData gameData, String playerColor) {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.gameData = gameData;
        this.playerColor = playerColor;
    }

    public void run() {
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n>>> " + SET_TEXT_COLOR_GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            StringBuilder board = (Objects.equals(playerColor, "white")) ? drawBoardWhite() : drawBoardBlack();
            return switch (cmd) {
                case "show" -> board.toString();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                - show - board
                - quit
                - help - see these options again
                """;
    }

    private StringBuilder drawBoardWhite() {
        StringBuilder boardString = new StringBuilder();
        ArrayList<ChessPiece> pieces = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = gameData.game().getBoard().getPiece(new ChessPosition(i, j));
                pieces.add(piece);
            }
        }
        int colorCtr = 0;
        for (int i=0; i < 64; i++) {

            ChessPiece piece = pieces.get(i);
            
            if ((i % 8) == 0) {
                boardString.append(RESET_BG_COLOR + "\n");
                colorCtr++;
            }

            String pieceIcon = getPieceIcon(piece);

            boardString.append(((i+colorCtr) % 2 != 0) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
            boardString.append(" ").append(pieceIcon).append(" ");

        }
        boardString.append(RESET_BG_COLOR + "\n");
        return boardString;
    }

    private static String getPieceIcon(ChessPiece piece) {
        String pieceIcon;

        if (piece == null) {
            pieceIcon = EMPTY;
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pieceIcon = (piece.getPieceType() == ChessPiece.PieceType.PAWN) ? WHITE_PAWN :
                    (piece.getPieceType() == ChessPiece.PieceType.ROOK) ? WHITE_ROOK :
                    (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) ? WHITE_KNIGHT :
                    (piece.getPieceType() == ChessPiece.PieceType.BISHOP) ? WHITE_BISHOP :
                    (piece.getPieceType() == ChessPiece.PieceType.KING) ? WHITE_KING : WHITE_QUEEN;
        }
        else {
            pieceIcon = (piece.getPieceType() == ChessPiece.PieceType.PAWN) ? BLACK_PAWN :
                    (piece.getPieceType() == ChessPiece.PieceType.ROOK) ? BLACK_ROOK :
                    (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) ? BLACK_KNIGHT :
                    (piece.getPieceType() == ChessPiece.PieceType.BISHOP) ? BLACK_BISHOP :
                    (piece.getPieceType() == ChessPiece.PieceType.KING) ? BLACK_KING : BLACK_QUEEN;
        }
        return pieceIcon;
    }

    private StringBuilder drawBoardBlack() {
        return new StringBuilder();
    }

}

