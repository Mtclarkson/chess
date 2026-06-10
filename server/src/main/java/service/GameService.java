package service;

import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.*;
import model.*;

import java.util.ArrayList;

public class GameService {
    private static GameDAO gameDAO = null;

    public GameService(GameDAO gameDAO) {
        GameService.gameDAO = gameDAO;
    }

    private boolean isNullOrBlank(String input) {
        return (input == null) || (input.isEmpty());
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        if (gameDAO.listGames().isEmpty()) {
            return new ArrayList<>();
        }
        return gameDAO.listGames();
    }

    public GameData createGame(String gameName) throws DataAccessException {
        if (isNullOrBlank(gameName)) {
            return null;
        }
        return gameDAO.createGame(gameName);
    }

    public GameData getGame(int givenGameID) throws DataAccessException {
        return gameDAO.getGame(givenGameID);
    }

    public GameData updateGame(String playerColor, String newUsername, int gameID, ChessMove move, boolean endGame)
            throws DataAccessException, InvalidMoveException {
        if (isNullOrBlank(playerColor) || isNullOrBlank(newUsername) || (getGame(gameID)==null) ||
                (!playerColor.equals("BLACK") && !playerColor.equals("WHITE"))) {
            return null;
        }
        return gameDAO.updateGame(playerColor, newUsername, gameID, move, endGame);
    }

    public void clearAllGames() throws DataAccessException {
        gameDAO.clearAllGames();
    }
}