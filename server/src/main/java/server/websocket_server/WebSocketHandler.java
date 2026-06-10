package server.websocket_server;

import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import service.AuthService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.*;
import service.GameService;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static websocket.commands.UserGameCommand.CommandType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;
    private final AuthService authService;
    private final UserService userService;

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameService = new GameService(gameDAO);
        this.authService = new AuthService(authDAO);
        this.userService = new UserService(userDAO);
    }


    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            MakeMoveCommand moveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command.getAuthToken(), command.getGameID(), ctx.session);
                case MAKE_MOVE ->
                        makeMove(moveCommand.getAuthToken(), moveCommand.getGameID(),
                        ctx.session, moveCommand.getMove());
                case LEAVE -> leave(command.getAuthToken(), command.getGameID(), ctx.session);
                case RESIGN -> resign(command.getAuthToken(), command.getGameID(), ctx.session);
            }
        } catch (IOException | DataAccessException | InvalidMoveException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connect(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        connections.add(session, gameID);
        GameData gameData = gameService.getGame(gameID);
        Gson gson = new Gson();
        if ((authService.getAuth(authToken) != null) && (gameService.getGame(gameID) != null)) {
            String game =
                    gson.toJson(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                            gameData));
            connections.reply(session, game);
            String message =
                    gson.toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "playerColor or Observer"));
            connections.broadcast(session, gameID, message);
        }
        else {
            String errorMessage =
                    gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: not implemented"));

            connections.reply(session, errorMessage);
        }
    }

    public void makeMove(String authToken, int gameID, Session session, ChessMove move)
            throws IOException, DataAccessException, InvalidMoveException {
        GameData gameData = gameService.getGame(gameID);
        AuthData authData = authService.getAuth(authToken);
        Gson gson = new Gson();
        if ((authData != null) && (gameData != null)) {
            String username = authData.username();

            ChessGame game = gameData.game();
            ChessGame.TeamColor playerColor = (username.equals(gameData.whiteUsername())) ?
                    ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            ChessBoard board = game.getBoard();
            if (game.getBoard().getPiece(move.getStartPosition()).getTeamColor() != playerColor) {
                String errorMessage =
                        gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                                "Error: Not your piece!"));
                connections.reply(session, errorMessage);
                return;
            }

            try {
                gameService.updateGame(playerColor.toString(), username, gameID, move, false);
            } catch (InvalidMoveException ex) {
                String errorMessage =
                        gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex));
                connections.reply(session, errorMessage);
                return;
            }

            GameData updatedGameData = gameService.getGame(gameID);
            ChessGame updatedGame = updatedGameData.game();

            String gameString =
                    gson.toJson(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGameData));
            connections.broadcast(session, gameID, gameString);
            connections.reply(session, gameString);

            if (updatedGame.isInCheckmate(ChessGame.TeamColor.WHITE) || updatedGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                String messageContent = (updatedGame.getTeamTurn() == playerColor) ? "You lost" : "You won!";
                String checkmateMessage =
                        gson.toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                "Checkmate!"));
                String extraMessage =
                        gson.toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                messageContent));
                connections.broadcast(session, gameID, checkmateMessage);
                connections.reply(session, checkmateMessage);
                connections.broadcast(session, gameID, extraMessage); //what the heck is the extra notif supposed to be
            } else {
                String message =
                        gson.toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                "playerColor or Observer"));
                connections.broadcast(session, gameID, message);
            }
        }
        else {
            String errorMessage =
                    gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: not implemented"));

            connections.reply(session, errorMessage);
        }
    }

    public void leave(String authToken, int gameID, Session session)
            throws IOException, DataAccessException, InvalidMoveException {
        Gson gson = new Gson();
        AuthData authData = authService.getAuth(authToken);
        String playerColor = (Objects.equals(authData.username(), gameService.getGame(gameID).whiteUsername())) ?
                "WHITE" : "BLACK";
        gameService.updateGame(playerColor,"leftGame",gameID,null, false);
        String message =
                gson.toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        String.format("%s has left", authData.username())));
        connections.broadcast(session, gameID, message);
        connections.remove(session, gameID);
    }

    public void resign(String authToken, int gameID, Session session)
            throws IOException, DataAccessException, InvalidMoveException {
        Gson gson = new Gson();
        AuthData authData = authService.getAuth(authToken);
        GameData gameData = gameService.getGame(gameID);
        String username = authData.username();
        if (!username.equals(gameService.getGame(gameID).whiteUsername()) &&
                !username.equals(gameService.getGame(gameID).blackUsername())) {
            String errorMessage =
                    gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                            "Error: observers may not resign"));
            connections.reply(session, errorMessage);
            return;
        }
        if (gameData.game().getGameOverStatus()) {
            String errorMessage =
                    gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                            "Error: already resigned"));
            connections.reply(session, errorMessage);
            return;
        }
        String playerColor = (Objects.equals(authData.username(), gameService.getGame(gameID).whiteUsername())) ?
                "WHITE" : "BLACK";
        gameService.updateGame(playerColor, authData.username(), gameID,null, true);
        String message =
                gson.toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        String.format("%s has resigned", authData.username())));
        connections.broadcast(session, gameID, message);
        connections.reply(session, message);
    }
}