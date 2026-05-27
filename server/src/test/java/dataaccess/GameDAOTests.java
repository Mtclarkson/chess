package dataaccess;

import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameDAOTests {

    public GameDAOTests() throws DataAccessException {
    }

    private final GameDAO db = new GameSQLDatabase();

    @BeforeEach
    void clear() throws DataAccessException {
        db.clearAllGames();
    }

    // positive createGame
    @Test
    void createGameTest() throws DataAccessException {
        db.createGame("game1");
        db.createGame("game2");

        assertEquals(2, db.listGames().size());
    }

    // negative createGame
    @Test
    void createGameBadRequestTest() throws DataAccessException {
        db.createGame("");
        assertEquals(0, db.listGames().size());
    }

    // positive getUser
    @Test
    void getGameTest() throws DataAccessException {
        GameData createdUser = db.createGame("bruh");
        GameData retrievedUser = db.getGame(createdUser.gameID());
        assertGamesEqual(createdUser, retrievedUser);
    }

    // negative createUser
    @Test
    void getGameDoesntExistTest() throws DataAccessException {
        db.createGame("bruh");
        GameData retrievedGame = db.getGame(67);
        assertNull(retrievedGame);
    }

    // positive listUsers
    @Test
    void listUsersTest() throws DataAccessException {
        ArrayList<GameData> expectedList = new ArrayList<>();

        GameData data1 = db.createGame("Fee Fi");
        GameData data2 = db.createGame("Fo Fum");

        expectedList.add(data1);
        expectedList.add(data2);

        assertGameCollectionEqual(expectedList, db.listGames());
    }

    // negative listUsers
    @Test
    void listUsersBadRequestsTest() throws DataAccessException {
        db.createGame("");
        db.createGame("");

        assertEquals(0, db.listGames().size());
    }

    // positive clearAllUsers
    @Test
    void clearAllUsersTest() throws DataAccessException {
        db.createGame("game1");
        db.createGame("game2");
        db.clearAllGames();

        assertEquals(0, db.listGames().size());
    }

    public static void assertGamesEqual(GameData expected, GameData actual) {
        assertEquals(expected.gameName(), actual.gameName());
        assertEquals(expected.gameID(), actual.gameID());
    }

    public static void assertGameCollectionEqual(Collection<GameData> expected, Collection<GameData> actual) {
        GameData[] actualList = actual.toArray(new GameData[]{});
        GameData[] expectedList = expected.toArray(new GameData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (int i = 0; i < actualList.length; i++) {
            assertGamesEqual(expectedList[i], actualList[i]);
        }
    }
}
