package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class AuthMemory implements AuthDAO {
    final private ArrayList<AuthData> authTokens = new ArrayList<>();

    public AuthData getAuth(String givenAuthToken) throws DataAccessException {
        for (AuthData authData : authTokens) {
            if (authData.authToken().equals(givenAuthToken)) {
                return authData;
            }
        }
        return null;
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        authData = new AuthData(authData.authToken(), authData.username());
        authTokens.add(authData);
        return authData;
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        authTokens.remove(authData);
    }

    public void clearAllAuthTokens() {authTokens.clear();}
}
