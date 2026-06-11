package client;


import chess.*;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import model.GameData;
import server.ServerFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.*;

import static ui.EscapeSequences.*;

public class GameplayClient implements NotificationHandler {
    private final ServerFacade server;
    private final String authToken;
    private final String playerColor;
    private GameData gameData;
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
    private final WebSocketFacade ws;

    // make a MakeMoveCommand and send it to the server
    public GameplayClient(String serverUrl, String authToken, GameData gameData, String playerColor)
            throws Exception {
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.gameData = gameData;
        this.playerColor = playerColor;
        ws = new WebSocketFacade(serverUrl, this);
    }

    public void run() throws Exception {
        System.out.print(help());
        ws.connect(authToken, gameData.gameID());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("leaving game")) {
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
        System.out.print(SET_TEXT_COLOR_BLUE + "\n>>> ");
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "show" -> drawBoard(playerColor, null).toString();
                case "moves" -> drawBoard(playerColor, params[0]).toString();
                case "move" -> move(params);
                case "resign" -> resign();
                case "leave" -> leave();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                - show - board
                - move <start> <end> - make a move
                - moves <piece position> - see all available moves for a piece
                - leave
                - resign
                - help - see these options again
                """;
    }

    private boolean inbounds(int i,int j) {
        return (i < 9) && (i > 0) && (j < 9) && (j > 0);
    }

    private String move(String... params) throws Exception {
        if ((params.length == 2) || (params.length == 3)) {
            String startPositionString = params[0];
            String endPositionString = params[1];
            ChessPiece.PieceType promotionPiece = (params.length == 3) ?
                    ChessPiece.PieceType.valueOf(params[2].toUpperCase()) : null;

            if ((startPositionString.length() != 2) ||
                    !Character.isAlphabetic(startPositionString.charAt(0)) ||
                    !Character.isDigit(startPositionString.charAt(1)) ||
                    (endPositionString.length() != 2) ||
                    !Character.isAlphabetic(endPositionString.charAt(0)) ||
                    !Character.isDigit(startPositionString.charAt(1))) {
                throw new Exception("acceptable move example: d2 d3");
            }
            int startRow = Integer.parseInt(String.valueOf(startPositionString.charAt(1)));
            int startCol = positionMap.get(startPositionString.charAt(0));
            int endRow = Integer.parseInt(String.valueOf(endPositionString.charAt(1)));
            int endCol = positionMap.get(endPositionString.charAt(0));

            if (inbounds(startRow, startCol) && inbounds(endRow, endCol)) {
                ChessPosition startPosition = new ChessPosition(startRow, startCol);
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);

                ws.makeMove(authToken, gameData.gameID(), move);
                return "";
            }
            else {
                throw new Exception("Move of Bounds");
            }
        }
        throw new Exception("Expected: <starting position> <ending position>");
    }

    private String resign() throws Exception {
        if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)||
                gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)||
                gameData.game().isInStalemate(ChessGame.TeamColor.WHITE)||
                gameData.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
            System.out.print("Game is over.");
            return "";
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Confirm resignation: y/n");
        printPrompt();
        String line = scanner.nextLine();
        if (Objects.equals(line, "y")) {
            ws.resign(authToken, gameData.gameID());

        }
        return "";
    }

    private String leave() throws Exception {
        ws.leave(authToken, gameData.gameID());
        return "leaving game";
    }

    private StringBuilder drawBoard(String playerColor, String piecePosition) {
        StringBuilder boardString = new StringBuilder();
        Collection<ChessPosition> endPositions = null;

        if (piecePosition != null) {
            ChessPosition selectedPiece;
            int pieceRow = Integer.parseInt(String.valueOf(piecePosition.charAt(1)));
            int pieceCol = positionMap.get(piecePosition.charAt(0));
            selectedPiece = new ChessPosition(pieceRow, pieceCol);
            ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) gameData.game().validMoves(selectedPiece);
            endPositions = new ArrayList<>();
            for (ChessMove validMove : validMoves) {
                endPositions.add(validMove.getEndPosition());
            }
        }

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
                ChessPosition selectedPosition = new ChessPosition(row, pieceCol);
                ChessPiece piece = gameData.game().getBoard().getPiece(selectedPosition);
                String pieceIcon = getPieceIcon(piece);
                if ((piecePosition != null) && (endPositions != null)) {
                    if (endPositions.contains(selectedPosition)) {
                        boardString.append(((colorCtr + colorFactor) % 2 == 0) ?
                                SET_BG_COLOR_GREEN : SET_BG_COLOR_DARK_GREEN);
                    } else {
                        boardString.append(((colorCtr + colorFactor) % 2 == 0) ?
                                SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
                    } colorCtr++;
                } else {
                    boardString.append(((colorCtr + colorFactor) % 2 == 0) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
                    colorCtr++;
                }
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

    public void notify(NotificationMessage notification) {
        System.out.println(SET_TEXT_COLOR_YELLOW + notification.getMessage());
        printPrompt();
    }

    public void thrower(ErrorMessage message) {
        System.out.println(SET_TEXT_COLOR_MAGENTA + message.getErrorMessage());
        printPrompt();
    }

    public void game(LoadGameMessage game) {
        System.out.print("\n");
        gameData = game.getGame();
        System.out.println(drawBoard(playerColor, null));
        printPrompt();
    }
}

