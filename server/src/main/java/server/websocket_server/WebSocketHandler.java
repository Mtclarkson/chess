package server.websocket_server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;

import static websocket.commands.UserGameCommand.CommandType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
                case CONNECT -> connect(command.getGameID(), ctx.session);
//                case MAKE_MOVE -> makeMove();
                case LEAVE -> leave(ctx.session);
//                case RESIGN -> resign();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(int gameID, Session session) throws IOException {
        connections.add(session);
        var broadcastMessage = String.format("Username joined as playerColor in %d", gameID);
        var game = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
//        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage);
//        connections.broadcast(session, notification); // how to get message info, username, playercolor
        connections.reply(session, game);
    }

    private void makeMove(String gameID, Session session) throws IOException {
        var message = ("%s left the game");
//        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
//        connections.broadcast(session, notification);
        connections.remove(session);
    }

    private void leave(Session session) throws IOException {
        var message = ("guy left the game");
//        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
//        connections.broadcast(session, notification);
        connections.remove(session);
    }
}