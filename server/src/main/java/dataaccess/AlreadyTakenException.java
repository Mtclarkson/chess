package dataaccess;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Username was already taken
 */
public class AlreadyTakenException extends Exception {
    public AlreadyTakenException(String message) {
        super(message);
    }
}