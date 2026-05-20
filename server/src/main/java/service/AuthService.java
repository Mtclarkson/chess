package service;

import dataaccess.*;
import model.*;

public class AuthService {
    private static AuthDAO authDAO = null;

    public AuthService(AuthDAO authDAO) {
        AuthService.authDAO = authDAO;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken);
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        return authDAO.createAuth(authData);
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        authDAO.deleteAuth(authData);
    }

    public void clearAllAuthTokens() throws DataAccessException {
        authDAO.clearAllAuthTokens();
    }
}
