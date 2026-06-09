package server.websocket_server;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;
import websocket.commands.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();

    public void add(Session session) {
        connections.put(session, session);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, String notification) throws IOException {
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(notification);
                }
            }
        }
    }

    public void reply(Session currentSession, String notification) throws IOException {
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (c.equals(currentSession)) {
                    c.getRemote().sendString(notification);
                }
            }
        }
    }
}
