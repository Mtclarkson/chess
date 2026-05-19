package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthMemory implements AuthDAO {
    final private HashMap<Integer, AuthData> authTokens = new HashMap<>();

    public void clearAllAuthTokens() {authTokens.clear();}
}
