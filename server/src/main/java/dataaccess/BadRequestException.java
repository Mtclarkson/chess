package dataaccess;

/**
 * Username was already taken
 */
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}