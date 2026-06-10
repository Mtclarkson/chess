package client;


import chess.ChessGame;
import chess.ChessMove;
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
    private final Map<Character, Integer> positionMap = Map.of(
            'a',1,
            'b', 2,
            'c', 3,
            'd', 4,
            'e', 5,
            'f', 6,
            'g', 7,
            'h', 8
    );

    // make a MakeMoveCommand and send it to the server
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
        System.out.print("\n>>> " + SET_TEXT_COLOR_YELLOW);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "show" -> drawBoard(playerColor).toString();
                case "move" -> move(params);
                case "leave" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                - show - board
                - leave
                - help - see these options again
                """;
    }

    private String move(String... params) throws Exception {
        if ((params.length == 2) || (params.length == 3)) {
            String startPositionString = params[0];
            String endPositionString = params[1];
            ChessPiece.PieceType promotionPiece =
                    (params[2].isEmpty()) ? null : ChessPiece.PieceType.valueOf(params[2].toUpperCase());
            ChessPosition startPosition =
                    new ChessPosition(startPositionString.charAt(1),positionMap.get(startPositionString.charAt(0)));
            ChessPosition endPosition =
                    new ChessPosition(endPositionString.charAt(1),positionMap.get(endPositionString.charAt(0)));
            ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);
            return move.toString(); // replace with websocketfacade
        }
        throw new Exception("Expected: <starting position> <ending position>");
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

