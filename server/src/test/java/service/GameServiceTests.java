package service;

import dataaccess.DataAccessException;
import dataaccess.GameMemory;
import dataaccess.UserMemory;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameServiceTests {

    static final GameService service = new GameService(new GameMemory());

    @BeforeEach
    void clear() throws DataAccessException {
        service.clearAllGames();
    }

    // positive test for createGame();
    @Test
    void createGameTest() throws DataAccessException {
        service.createGame("monopoly");
        service.createGame("bonopoly");

        assertEquals(2, service.listGames().size());
    }

    // negative test for createGame();
    @Test
    void createGameBadRequestTest() throws DataAccessException {
        service.createGame("");
        service.createGame("");

        assertEquals(0, service.listGames().size());
    }

    // positive test for getGame();
    @Test
    void getGameTest() throws DataAccessException {
        GameData monopoly = service.createGame("monopoly");

        GameData result = service.getGame(monopoly.gameID());

        assertEquals(monopoly, result);
    }

    // negative test for getGame();
    @Test
    void getGameDoesntExistTest() throws DataAccessException {
        GameData monopoly = service.createGame("monopoly");
        GameData result = service.getGame(67);

        assertNull(result);
    }

    // positive test for updateGame();
    @Test
    void updateGameTest() throws DataAccessException {
        GameData monopoly = service.createGame("monopoly");
        monopoly = service.updateGame("BLACK","Mr. White", monopoly.gameID());
        monopoly = service.updateGame("WHITE","Mr. Black", monopoly.gameID());

        assertEquals("Mr. White", monopoly.blackUsername());
    }

    // negative test for updateGame();
    @Test
    void updateGameDoesntExistTest() throws DataAccessException {
        GameData update;
        int fakeID = 67;
        update = service.updateGame("BLACK","Mr. White", fakeID);

        assertNull(update);
    }

    // positive test for listGames();
    @Test
    void listGamesTest() throws DataAccessException {
        service.createGame("monopoly");
        GameData game2 = service.createGame("bonopoly");
        ArrayList<GameData> list = service.listGames();
        assertEquals(game2, list.get(1));
    }

    // positive test for clearAllGames();
    @Test
    void clearGamesTest() throws DataAccessException {
        service.createGame("monopoly");
        service.createGame("bonopoly");

        service.clearAllGames();
        assertEquals(0, service.listGames().size());
    }

}