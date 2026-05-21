package service;

import dataaccess.AuthMemory;
import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthServiceTests {

    static final AuthService AUTH_SERVICE = new AuthService(new AuthMemory());

    @BeforeEach
    void clear() throws DataAccessException {
        AUTH_SERVICE.clearAllAuthTokens();
    }

    // positive test for createAuth();
    @Test
    void createAuthTest() throws DataAccessException {
        AUTH_SERVICE.createAuth(new AuthData("randomtoken","username"));
        AUTH_SERVICE.createAuth(new AuthData("randomtoken2","username2"));

        assertEquals(2, AUTH_SERVICE.authList().size());
    }

    // negative test for createAuth();
    @Test
    void createAuthBadRequestTest() throws DataAccessException {
        AUTH_SERVICE.createAuth(new AuthData("randomtoken",""));
        AUTH_SERVICE.createAuth(new AuthData("","username2"));

        assertEquals(0, AUTH_SERVICE.authList().size());
    }

    // positive test for getAuth();
    @Test
    void getAuthTest() throws DataAccessException {
        AuthData expected = AUTH_SERVICE.createAuth(new AuthData("randomtoken","username"));
        AuthData actual = AUTH_SERVICE.getAuth("randomtoken");
        assertEquals(expected, actual);
    }

    // negative test for getAuth();
    @Test
    void getAuthDoesntExistTest() throws DataAccessException {
        AuthData actual = AUTH_SERVICE.getAuth("randomtoken");
        assertNull(actual);
    }

    // positive test for deleteAuth();
    @Test
    void deleteAuthTest() throws DataAccessException {
        AuthData auth1 = AUTH_SERVICE.createAuth(new AuthData("randomtoken","username"));
        AuthData auth2 = AUTH_SERVICE.createAuth(new AuthData("randomtoken2","username2"));

        AUTH_SERVICE.deleteAuth(auth1);
        AUTH_SERVICE.deleteAuth(auth2);

        assertEquals(0, AUTH_SERVICE.authList().size());
    }

    // negative test for deleteAuth();
    @Test
    void deleteAuthAlreadyDeletedTest() throws DataAccessException {
        AuthData auth1 = AUTH_SERVICE.createAuth(new AuthData("randomtoken","username"));
        AuthData auth2 = AUTH_SERVICE.createAuth(new AuthData("randomtoken2","username2"));

        AUTH_SERVICE.deleteAuth(auth1);
        AUTH_SERVICE.deleteAuth(auth1);
        assertEquals(1, AUTH_SERVICE.authList().size());
    }

    // positive test for clearAllAuthTokens();
    @Test
    void clearALlAuthTokensTest() throws DataAccessException {
        AUTH_SERVICE.createAuth(new AuthData("randomtoken","username"));
        AUTH_SERVICE.createAuth(new AuthData("randomtoken2","username2"));

        AUTH_SERVICE.clearAllAuthTokens();
        assertEquals(0, AUTH_SERVICE.authList().size());
    }

}