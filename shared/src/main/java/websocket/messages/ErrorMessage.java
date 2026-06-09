package websocket.messages;

/**
 * Error messages from server to client
 */
public class ErrorMessage extends ServerMessage {

    String message;
    public ErrorMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

}
