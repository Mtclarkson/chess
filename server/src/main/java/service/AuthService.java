package service;

import dataaccess.*;
import model.*;

public class AuthService {
    private static AuthDAO authDAO = null;

    public AuthService(AuthDAO authDAO) {
        AuthService.authDAO = authDAO;
    }

    public static AuthData createAuth(AuthData authData) throws DataAccessException {
        return authDAO.createAuth(authData);
    }

    public static void clearAllAuthTokens() throws DataAccessException {
        authDAO.clearAllAuthTokens();
    }
}
