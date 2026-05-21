package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;

    AuthData createAuth(AuthData authData) throws DataAccessException;

    void deleteAuth(AuthData authData) throws DataAccessException;

    ArrayList<AuthData> authList() throws DataAccessException;

    void clearAllAuthTokens() throws DataAccessException;
}
