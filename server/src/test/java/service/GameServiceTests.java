package service;

import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.GameMemory;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameServiceTests {

    static final GameService GAME_SERVICE = new GameService(new GameMemory());

    @BeforeEach
    void clear() throws DataAccessException {
        GAME_SERVICE.clearAllGames();
    }

    // positive test for createGame();
    @Test
    void createGameTest() throws DataAccessException {
        GAME_SERVICE.createGame("monopoly");
        GAME_SERVICE.createGame("bonopoly");

        assertEquals(2, GAME_SERVICE.listGames().size());
    }

    // negative test for createGame();
    @Test
    void createGameBadRequestTest() throws DataAccessException {
        GAME_SERVICE.createGame("");
        GAME_SERVICE.createGame("");

        assertEquals(0, GAME_SERVICE.listGames().size());
    }

    // positive test for getGame();
    @Test
    void getGameTest() throws DataAccessException {
        GameData monopoly = GAME_SERVICE.createGame("monopoly");

        GameData result = GAME_SERVICE.getGame(monopoly.gameID());

        assertEquals(monopoly, result);
    }

    // negative test for getGame();
    @Test
    void getGameDoesntExistTest() throws DataAccessException {
        GameData monopoly = GAME_SERVICE.createGame("monopoly");
        GameData result = GAME_SERVICE.getGame(67);

        assertNull(result);
    }

    // positive test for updateGame();
    @Test
    void updateGameTest() throws DataAccessException, InvalidMoveException {
        GameData monopoly = GAME_SERVICE.createGame("monopoly");
        monopoly = GAME_SERVICE.updateGame("BLACK","Mr. White", monopoly.gameID(), null);
        monopoly = GAME_SERVICE.updateGame("WHITE","Mr. Black", monopoly.gameID(), null);

        assertEquals("Mr. White", monopoly.blackUsername());
    }

    // negative test for updateGame();
    @Test
    void updateGameDoesntExistTest() throws DataAccessException, InvalidMoveException {
        GameData update;
        int fakeID = 67;
        update = GAME_SERVICE.updateGame("BLACK","Mr. White", fakeID, null);

        assertNull(update);
    }

    // positive test for listGames();
    @Test
    void listGamesTest() throws DataAccessException {
        GAME_SERVICE.createGame("monopoly");
        GameData game2 = GAME_SERVICE.createGame("bonopoly");
        ArrayList<GameData> list = GAME_SERVICE.listGames();
        assertEquals(game2, list.get(1));
    }

    // positive test for clearAllGames();
    @Test
    void clearGamesTest() throws DataAccessException {
        GAME_SERVICE.createGame("monopoly");
        GAME_SERVICE.createGame("bonopoly");

        GAME_SERVICE.clearAllGames();
        assertEquals(0, GAME_SERVICE.listGames().size());
    }

}