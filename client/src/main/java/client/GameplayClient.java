package client;


import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import server.ServerFacade;

import java.util.*;

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
            return switch (cmd) {
                case "show" -> drawBoard(playerColor).toString();
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

    private StringBuilder drawBoard(String playerColor) {
        StringBuilder boardString = new StringBuilder();

        String headerLetters = (Objects.equals(playerColor, "black")) ?
                "     h    g    f    e    d    c    b    a     " :
                "     a    b    c    d    e    f    g    h     ";

        boardString.append(SET_BG_COLOR_LIGHT_GREY).append(headerLetters).append(RESET_BG_COLOR + "\n");
        int startingRow = (Objects.equals(playerColor, "black")) ? 1 : 8;
        int endingRow = (Objects.equals(playerColor, "black")) ? 9 : 0;
        int incFactor = (Objects.equals(playerColor, "black")) ? 1 : -1;
        int row,col;
        int colorCtr=0;
        int colorFactor=0;
        int pieceCol;
        boardString.append(SET_BG_COLOR_LIGHT_GREY).append(" ").append(startingRow).append(" ");
        for (row = startingRow; row != endingRow; row+=incFactor) {
                if (row != startingRow) {
                    boardString.append(SET_BG_COLOR_LIGHT_GREY).append(" ").append(row-incFactor).append(" ");
                    boardString.append(RESET_BG_COLOR + "\n");
                    boardString.append(SET_BG_COLOR_LIGHT_GREY).append(" ").append(row).append(" ");
                    colorFactor++;
                }
            for (col = 1; col < 9; col++) {
                pieceCol = (Objects.equals(playerColor, "black")) ? 9 - col : col;
                ChessPiece piece = gameData.game().getBoard().getPiece(new ChessPosition(row, pieceCol));
                String pieceIcon = getPieceIcon(piece);
                boardString.append(((colorCtr + colorFactor) % 2 == 0) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
                colorCtr++;
                boardString.append(" ").append(pieceIcon).append(" ");
            }
        } boardString.append(SET_BG_COLOR_LIGHT_GREY).append(" " + (9-startingRow) + " ").append(RESET_BG_COLOR + "\n");
        boardString.append(SET_BG_COLOR_LIGHT_GREY).append(headerLetters).append(RESET_BG_COLOR + "\n");

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
}

