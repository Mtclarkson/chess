package chess;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidMoveException extends Exception {
    String message;
    public InvalidMoveException(String message) {
        super(message);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
