package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;

public class GameService {
    private static GameDAO gameDAO = null;

    public GameService(GameDAO gameDAO) {
        GameService.gameDAO = gameDAO;
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        return gameDAO.listGames();
    }

    public GameData createGame(String gameName) throws DataAccessException {
        return gameDAO.createGame(gameName);
    }

    public GameData getGame(int givenGameID) throws DataAccessException {
        return gameDAO.getGame(givenGameID);
    }

    public void clearAllGames() throws DataAccessException {
        gameDAO.clearAllGames();
    }
}