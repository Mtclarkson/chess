package server.websocket_server;

import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.AuthService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.*;
import service.GameService;
import java.io.IOException;
import java.util.Map;

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
            switch (command.getCommandType()) {
                case CONNECT -> connect(command.getAuthToken(), command.getGameID(), ctx.session);
                case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), ctx.session, move);
                case LEAVE -> leave(ctx.session);
//                case RESIGN -> resign();
            }
        } catch (IOException | DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connect(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        connections.add(session);
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
            connections.broadcast(session, message);
        }
        else {
            String errorMessage =
                    gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: not implemented"));

            connections.reply(session, errorMessage);
        }
    }

    public void makeMove(String authToken, int gameID, Session session, ChessMove move) throws IOException, DataAccessException, InvalidMoveException {
        GameData gameData = gameService.getGame(gameID);
        Gson gson = new Gson();
        if ((authService.getAuth(authToken) != null) && (gameData != null)) {
            try {
                gameData.game().makeMove(move);
            } catch (InvalidMoveException ex) {
                String errorMessage =
                        gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex));
                connections.reply(session, errorMessage);
            }
            String game =
                    gson.toJson(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                            gameData));
            connections.reply(session, game);
            String message =
                    gson.toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "playerColor or Observer"));
            connections.broadcast(session, message);
        }
        else {
            String errorMessage =
                    gson.toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: not implemented"));

            connections.reply(session, errorMessage);
        }
    }

    public void leave(Session session) throws IOException {
        var message = ("guy left the game");
//        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
//        connections.broadcast(session, notification);
        connections.remove(session);
    }
}