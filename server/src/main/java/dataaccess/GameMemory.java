package dataaccess;

import model.GameData;

import java.util.HashMap;

public class GameMemory implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();
}
