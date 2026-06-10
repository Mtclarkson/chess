package client.websocket;

import websocket.messages.*;

public interface NotificationHandler {
    void notify(NotificationMessage message);
    void thrower(ErrorMessage message);
    void game(LoadGameMessage game);
}

