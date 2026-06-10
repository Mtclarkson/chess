package server.websocket_server;

import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;
import websocket.commands.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, List<Session>> connections = new ConcurrentHashMap<>();

    public void add(Session session, int gameID) {
        connections.computeIfAbsent(gameID, _ -> new ArrayList<>()).add(session);
    }

    public void remove(Session session, int gameID) {
        List<Session> sessions = connections.get(gameID);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    public void broadcast(Session excludeSession, int gameID, String notification) throws IOException {
        List<Session> sessions = connections.getOrDefault(gameID, new ArrayList<>());
        for (Session c : sessions) {
            if (c.isOpen() && !c.equals(excludeSession)) {
                c.getRemote().sendString(notification);
            }
        }
    }

    public void reply(Session currentSession, String notification) throws IOException {
        for (List<Session> sessions : connections.values()) {
            for (Session c : sessions) {
                if (c.isOpen() && c.equals(currentSession)) {
                    c.getRemote().sendString(notification);
                }
            }
        }
    }
}
