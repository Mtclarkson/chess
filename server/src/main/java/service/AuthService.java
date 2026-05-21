package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;

public class AuthService {
    private static AuthDAO authDAO = null;

    public AuthService(AuthDAO authDAO) {
        AuthService.authDAO = authDAO;
    }

    private boolean isNullOrBlank(String input) {
        return (input == null) || (input.isEmpty());
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        if (isNullOrBlank(authToken)) {
            return null;
        }
        return authDAO.getAuth(authToken);
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        if ((authData == null) || isNullOrBlank(authData.authToken()) || isNullOrBlank(authData.username())) {
            return null;
        }
        return authDAO.createAuth(authData);
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        if ((authData == null) || isNullOrBlank(authData.authToken()) || isNullOrBlank(authData.username())) {
            return;
        }
        authDAO.deleteAuth(authData);
    }

    // for JUnit testing only
    public ArrayList<AuthData> authList() throws DataAccessException {
        return authDAO.authList();
    }

    public void clearAllAuthTokens() throws DataAccessException {
        authDAO.clearAllAuthTokens();
    }
}
