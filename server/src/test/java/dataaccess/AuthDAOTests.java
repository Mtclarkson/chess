package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AuthDAOTests {

    public AuthDAOTests() throws DataAccessException {
    }

    private final AuthDAO db = new AuthSQLDatabase();

    @BeforeEach
    void clear() throws DataAccessException {
        db.clearAllAuthTokens();
    }

    // positive createAuth
    @Test
    void createAuthTest() throws DataAccessException {
        db.createAuth(new AuthData("randomtoken","username"));
        db.createAuth(new AuthData("randomtoken2","username2"));

        assertEquals(2, db.authList().size());
    }

    // negative createAuth
    @Test
    void createAuthBadRequestTest() throws DataAccessException {
        db.createAuth(new AuthData("randomtoken",""));
        db.createAuth(new AuthData("","username2"));

        assertEquals(0, db.authList().size());
    }

    // positive getAuth
    @Test
    void getAuthTest() throws DataAccessException {
        AuthData expected = db.createAuth(new AuthData("randomtoken","username"));
        AuthData actual = db.getAuth("randomtoken");
        assertEquals(expected, actual);
    }

    // negative createAuth
    @Test
    void getAuthDoesntExistTest() throws DataAccessException {
        AuthData actual = db.getAuth("randomtoken");
        assertNull(actual);
    }

    // positive deleteAuths
    @Test
    void deleteAuthsTest() throws DataAccessException {
        AuthData auth1 = db.createAuth(new AuthData("randomtoken","username"));
        AuthData auth2 = db.createAuth(new AuthData("randomtoken2","username2"));

        db.deleteAuth(auth1);
        db.deleteAuth(auth2);

        assertEquals(0, db.authList().size());
    }

    // negative test for deleteAuth();
    @Test
    void deleteAuthAlreadyDeletedTest() throws DataAccessException {
        AuthData auth1 = db.createAuth(new AuthData("randomtoken","username"));
        AuthData auth2 = db.createAuth(new AuthData("randomtoken2","username2"));

        db.deleteAuth(auth1);
        db.deleteAuth(auth1);
        assertEquals(1, db.authList().size());
    }

    @Test
    void listAuthsTest() throws DataAccessException {
        ArrayList<AuthData> expectedList = new ArrayList<>();

        AuthData auth1 = db.createAuth(new AuthData("randomtoken","username"));
        AuthData auth2 = db.createAuth(new AuthData("randomtoken2","username2"));

        expectedList.add(auth1);
        expectedList.add(auth2);

        assertAuthCollectionEqual(expectedList, db.authList());
    }

    // negative listUsers
    @Test
    void listUsersBadRequestsTest() throws DataAccessException {
        db.createAuth(new AuthData("","username"));
        db.createAuth(new AuthData("randomtoken2",""));

        assertEquals(0, db.authList().size());
    }

    // positive clearAllAuths
    @Test
    void clearAllAuthsTest() throws DataAccessException {
        db.createAuth(new AuthData("randomtoken","username"));
        db.createAuth(new AuthData("randomtoken2","username2"));
        db.clearAllAuthTokens();

        assertEquals(0, db.authList().size());
    }

    public static void assertAuthsEqual(AuthData expected, AuthData actual) {
        assertEquals(expected.username(), actual.username());
        // maybe also check email and password
    }

    public static void assertAuthCollectionEqual(Collection<AuthData> expected, Collection<AuthData> actual) {
        AuthData[] actualList = actual.toArray(new AuthData[]{});
        AuthData[] expectedList = expected.toArray(new AuthData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (int i = 0; i < actualList.length; i++) {
            assertAuthsEqual(expectedList[i], actualList[i]);
        }
    }
}
