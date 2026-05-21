package service;

import dataaccess.DataAccessException;
import dataaccess.UserMemory;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTests {

    static final UserService USER_SERVICE = new UserService(new UserMemory());

    @BeforeEach
    void clear() throws DataAccessException {
        USER_SERVICE.clearAllUsers();
    }

    // positive test for createUser();
    @Test
    void createUserTest() throws DataAccessException {
        USER_SERVICE.createUser(new UserData("Rocky","amaze","rky@erid.org"));
        USER_SERVICE.createUser(new UserData("Grace","astro","grace420@gmail.com"));

        assertEquals(2, USER_SERVICE.usersList().size());
    }

    // negative test for createUser();
    @Test
    void createUserBadRequestTest() throws DataAccessException {
        USER_SERVICE.createUser(new UserData("","amaze","rky@erid.org"));
        USER_SERVICE.createUser(new UserData("Grace","","grace420@gmail.com"));

        assertEquals(0, USER_SERVICE.usersList().size());
    }

    // positive test for getUser();
    @Test
    void getUserTest() throws DataAccessException {
        UserData expected = USER_SERVICE.createUser(new UserData("Grace","astro","grace420@gmail.com"));
        UserData data = USER_SERVICE.getUser("Grace");

        assertEquals(expected, data);
    }

    // negative test for getUser();
    @Test
    void getUserBadRequestTest() throws DataAccessException {
        USER_SERVICE.createUser(new UserData("Grace","astro","grace420@gmail.com"));
        UserData data = USER_SERVICE.getUser("");

        assertNull(data);
    }

    // positive test for clearAllUsers();
    @Test
    void clearAllUsersTest() throws DataAccessException {
        USER_SERVICE.createUser(new UserData("Rocky","amaze","rky@erid.org"));
        USER_SERVICE.createUser(new UserData("Grace","astro","grace420@gmail.com"));

        USER_SERVICE.clearAllUsers();
        assertEquals(0, USER_SERVICE.usersList().size());
    }
}