package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class GameSQLDatabase implements GameDAO {

    public GameSQLDatabase() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `gameData` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    private int id = 0;

    public GameData createGame(String gameName) throws DataAccessException {
        if (DatabaseManager.isNullOrBlank(gameName)) {
            return null;
        }
        id++;
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(id, null, null, gameName, newGame);
        String gameSerialized = new Gson().toJson(newGame);
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameData) " +
                "VALUES (?, ?, ?, ?, ?)";
        DatabaseManager.executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(),
                gameData.blackUsername(), gameData.gameName(), gameSerialized);
        return gameData;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    // add makeMove() functionality
    public GameData updateGame(String playerColor, String newUsername, int gameID, ChessMove move)
            throws DataAccessException, InvalidMoveException {
        GameData gameData = getGame(gameID);
        if (move == null) {
            GameData updatedGame;
            updatedGame = (playerColor.equals("WHITE")) ?
                    new GameData(gameID, newUsername, gameData.blackUsername(), gameData.gameName(), gameData.game()) :
                    new GameData(gameID, gameData.whiteUsername(), newUsername, gameData.gameName(), gameData.game());
            var statement = (playerColor.equals("WHITE")) ?
                    "UPDATE game SET whiteUsername = ? WHERE gameID = ?;" :
                    "UPDATE game SET blackUsername = ? WHERE gameID = ?;";
            DatabaseManager.executeUpdate(statement, newUsername, gameID);
            return updatedGame;
        } else {
            gameData.game().makeMove(move);
            Gson gson = new Gson();
            String gameJson = gson.toJson(gameData.game());
            var statement = "UPDATE game SET gameData = ? WHERE gameID = ?;";
            DatabaseManager.executeUpdate(statement, gameJson, gameID);
            return gameData;
        }
    }

    public void clearAllGames() throws DataAccessException {
        id = 0;
        var statement = "TRUNCATE game";
        DatabaseManager.executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        ChessGame game = new Gson().fromJson(rs.getString("gameData"), ChessGame.class);
        return new GameData(rs.getInt("gameID"),
                rs.getString("whiteUsername"), rs.getString("blackUsername"),
                rs.getString("gameName"), game);
    }

}
