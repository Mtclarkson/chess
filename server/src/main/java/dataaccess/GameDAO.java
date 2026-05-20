package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    ArrayList<GameData> listGames() throws DataAccessException;

    GameData createGame(String gameName) throws DataAccessException;

    void clearAllGames() throws DataAccessException;

}
