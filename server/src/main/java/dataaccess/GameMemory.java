package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class GameMemory implements GameDAO {
    final private ArrayList<GameData> games = new ArrayList<>();

    public ArrayList<GameData> listGames() {
        return games;
    }

    public GameData createGame(String gameName) {
        GameData game = new GameData(1234, "WHITE", "BLACK", gameName, new ChessGame());
        games.add(game);
        return game;
    }

    public void clearAllGames() {games.clear();}

}
