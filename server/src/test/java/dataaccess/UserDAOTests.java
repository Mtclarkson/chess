package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.AssertionsKt.assertDoesNotThrow;

public class UserDAOTests {

    public UserDAOTests() throws DataAccessException {
    }

    private final UserDAO db = new UserSQLDatabase();

    @BeforeEach
    void clear() throws DataAccessException {
        db.clearAllUsers();
    }

    // positive createUser
    @Test
    void createUserTest() throws DataAccessException {
        db.createUser(new UserData("newUsername", "joeMama", "joe@mama.org"));
        db.createUser(new UserData("otherUsername", "joeDada", "joe@dada.org"));

        assertEquals(2, db.usersList().size());
    }

    // negative createUser
    @Test
    void createUserBadRequestTest() throws DataAccessException {
        UserData newUser = new UserData("", "joeMama", "joe@mama.org");
        newUser = db.createUser(newUser);
        assertNull(newUser);
    }

    // positive getUser
    @Test
    void getUserTest() throws DataAccessException {
        UserData createdUser = db.createUser(new UserData("newUsername", "joeMama", "joe@mama.org"));
        UserData retrievedUser = db.getUser("newUsername");
        assertUsersEqual(createdUser, retrievedUser);
    }

    // negative createUser
    @Test
    void getUserDoesntExistTest() throws DataAccessException {
        db.createUser(new UserData("newUsername", "joeMama", "joe@mama.org"));
        UserData retrievedUser = db.getUser("wrongUsername");
        assertNull(retrievedUser);
    }

    // positive listUsers
    @Test
    void listUsersTest() throws DataAccessException {
        ArrayList<UserData> expectedList = new ArrayList<>();

        UserData data1 = db.createUser(new UserData("newUsername", "joeMama", "joe@mama.org"));
        UserData data2 = db.createUser(new UserData("otherUsername", "joeDada", "joe@dada.org"));

        expectedList.add(data1);
        expectedList.add(data2);

        assertUserCollectionEqual(expectedList, db.usersList());
    }

    // negative listUsers
    @Test
    void listUsersBadRequestsTest() throws DataAccessException {
        db.createUser(new UserData("newUsername", "", "joe@mama.org"));
        db.createUser(new UserData("", "joeDada", "joe@dada.org"));

        assertEquals(0, db.usersList().size());
    }

    // positive clearAllUsers
    @Test
    void clearAllUsersTest() throws DataAccessException {
        db.createUser(new UserData("newUsername", "joeMama", "joe@mama.org"));
        db.createUser(new UserData("otherUsername", "joeDada", "joe@dada.org"));
        db.clearAllUsers();

        assertEquals(0, db.usersList().size());
    }

    public static void assertUsersEqual(UserData expected, UserData actual) {
        assertEquals(expected.username(), actual.username());
        // maybe also check email and password
    }

    public static void assertUserCollectionEqual(Collection<UserData> expected, Collection<UserData> actual) {
        UserData[] actualList = actual.toArray(new UserData[]{});
        UserData[] expectedList = expected.toArray(new UserData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (int i = 0; i < actualList.length; i++) {
            assertUsersEqual(expectedList[i], actualList[i]);
        }
    }
}
