package dataaccess;

/**
 * Username was already taken
 */
public class AlreadyTakenException extends Exception {
    public AlreadyTakenException(String message) {
        super(message);
    }
}