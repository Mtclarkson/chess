package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class GameMemory implements GameDAO {
    final private ArrayList<GameData> games = new ArrayList<>();
    public int id = 0;

    public ArrayList<GameData> listGames() {
        return games;
    }

    public GameData createGame(String gameName) {
        id++;
        GameData game = new GameData(id, null, null, gameName, new ChessGame());
        games.add(game);
        return game;
    }

    public GameData getGame(int givenGameID) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == givenGameID) {
                return game;
            }
        }
        return null;
    }

    public GameData updateGame(String playerColor, String newUsername, int gameID) throws DataAccessException {
        GameData game = getGame(gameID);
        GameData updatedGame = (playerColor.equals("WHITE")) ? new GameData(gameID, newUsername, game.blackUsername() ,game.gameName(), game.game()) :
                new GameData(gameID, game.whiteUsername(), newUsername, game.gameName(), game.game());
        games.remove(game);
        games.add(updatedGame);
        return updatedGame;
    }

    public void clearAllGames() {
        games.clear();
        id = 0;
    }

}
