package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class GameMemory implements GameDAO {
    final private ArrayList<GameData> games = new ArrayList<>();

    public ArrayList<GameData> listGames() {
        return games;
    }

    GameData createGame(String gameName) {

    }

    public void clearAllGames() {games.clear();}

}
