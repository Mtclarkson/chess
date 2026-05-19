package service;

import dataaccess.*;
import model.*;

public class GameService {
    private static GameDAO gameDAO = null;

    public GameService(GameDAO gameDAO) {
        GameService.gameDAO = gameDAO;
    }

    public static void clearAllGames() throws DataAccessException {
        gameDAO.clearAllGames();
    }
}