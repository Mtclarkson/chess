package websocket.messages;

/**
 * notification message from server to client
 */
public class NotificationMessage extends ServerMessage {

    String message;
    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

}
