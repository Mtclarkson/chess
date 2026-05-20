package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class AuthMemory implements AuthDAO {
    final private ArrayList<AuthData> authTokens = new ArrayList<>();

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        authData = new AuthData(authData.authToken(), authData.username());
        authTokens.add(authData);
        return authData;
    }

    public void clearAllAuthTokens() {authTokens.clear();}
}
