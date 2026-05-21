package service;

import dataaccess.AuthMemory;
import dataaccess.DataAccessException;
import dataaccess.UserMemory;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthServiceTests {

    static final AuthService service = new AuthService(new AuthMemory());

    @BeforeEach
    void clear() throws DataAccessException {
        service.clearAllAuthTokens();
    }

    // positive test for createAuth();
    @Test
    void createAuthTest() throws DataAccessException {
        service.createAuth(new AuthData("randomtoken","username"));
        service.createAuth(new AuthData("randomtoken2","username2"));

        assertEquals(2, service.authList().size());
    }

    // negative test for createAuth();
    @Test
    void createAuthBadRequestTest() throws DataAccessException {
        service.createAuth(new AuthData("randomtoken",""));
        service.createAuth(new AuthData("","username2"));

        assertEquals(0, service.authList().size());
    }

    // positive test for getAuth();
    @Test
    void getAuthTest() throws DataAccessException {
        AuthData expected = service.createAuth(new AuthData("randomtoken","username"));
        AuthData actual = service.getAuth("randomtoken");
        assertEquals(expected, actual);
    }

    // negative test for getAuth();
    @Test
    void getAuthDoesntExistTest() throws DataAccessException {
        AuthData actual = service.getAuth("randomtoken");
        assertNull(actual);
    }

    // positive test for deleteAuth();
    @Test
    void deleteAuthTest() throws DataAccessException {
        AuthData auth1 = service.createAuth(new AuthData("randomtoken","username"));
        AuthData auth2 = service.createAuth(new AuthData("randomtoken2","username2"));

        service.deleteAuth(auth1);
        service.deleteAuth(auth2);

        assertEquals(0, service.authList().size());
    }

    // negative test for deleteAuth();
    @Test
    void deleteAuthAlreadyDeletedTest() throws DataAccessException {
        AuthData auth1 = service.createAuth(new AuthData("randomtoken","username"));
        AuthData auth2 = service.createAuth(new AuthData("randomtoken2","username2"));

        service.deleteAuth(auth1);
        service.deleteAuth(auth1);
        assertEquals(1, service.authList().size());
    }

    // positive test for clearAllAuthTokens();
    @Test
    void clearALlAuthTokensTest() throws DataAccessException {
        service.createAuth(new AuthData("randomtoken","username"));
        service.createAuth(new AuthData("randomtoken2","username2"));

        service.clearAllAuthTokens();
        assertEquals(0, service.authList().size());
    }

}