package websocket.messages;

/**
 * Error messages from server to client
 */
public class ErrorMessage extends ServerMessage {

    String errorMessage;
    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {return errorMessage;}


}
