package websocket.messages;

/**
 * game from server to client
 */
public class LoadGameMessage extends ServerMessage {

    String message;
    public LoadGameMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

}
