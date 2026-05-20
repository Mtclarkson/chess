package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UserMemory;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTests {

    static final UserService service = new UserService(new UserMemory());

    @BeforeEach
    void clear() throws DataAccessException {
        service.clearAllUsers();
    }

    // positive test for createUser();
    @Test
    void createUserTest() throws DataAccessException {
        service.createUser(new UserData("Rocky","amaze","rky@erid.org"));
        service.createUser(new UserData("Grace","astro","grace420@gmail.com"));

        assertEquals(2, service.usersList().size());
    }

    // negative test for createUser();
    @Test
    void createUserBadRequestTest() throws DataAccessException {
        service.createUser(new UserData("","amaze","rky@erid.org"));
        service.createUser(new UserData("Grace","","grace420@gmail.com"));

        assertEquals(0, service.usersList().size());
    }

    // positive test for getUser();
    @Test
    void getUserTest() throws DataAccessException {
        UserData expected = service.createUser(new UserData("Grace","astro","grace420@gmail.com"));
        UserData data = service.getUser("Grace");

        assertEquals(expected, data);
    }

    // negative test for getUser();
    @Test
    void getUserBadRequestTest() throws DataAccessException {
        service.createUser(new UserData("Grace","astro","grace420@gmail.com"));
        UserData data = service.getUser("");

        assertNull(data);
    }

    // positive test for clearAllUsers();
    @Test
    void clearAllUsersTest() throws DataAccessException {
        service.createUser(new UserData("Rocky","amaze","rky@erid.org"));
        service.createUser(new UserData("Grace","astro","grace420@gmail.com"));

        service.clearAllUsers();
        assertEquals(0, service.usersList().size());
    }
}