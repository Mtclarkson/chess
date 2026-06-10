package dataaccess;

import chess.ChessMove;
import chess.InvalidMoveException;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    ArrayList<GameData> listGames() throws DataAccessException;

    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(int givenGameID) throws DataAccessException;

    GameData updateGame(String playerColor, String username, int gameID, ChessMove move, boolean endGame)
            throws DataAccessException, InvalidMoveException;

    void clearAllGames() throws DataAccessException;

}
