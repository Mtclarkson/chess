package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class GameSQLDatabase implements GameDAO {

    public GameSQLDatabase() throws DataAccessException {
        configureDatabase();
    }

    private int id = 0;

    public GameData createGame(String gameName) throws DataAccessException {
        id++;
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(id, null, null, gameName, newGame);
        String gameSerialized = new Gson().toJson(newGame);
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameData) " +
                "VALUES (?, ?, ?, ?, ?)";
        executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(),
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

    public GameData updateGame(String playerColor, String newUsername, int gameID) throws DataAccessException {
        GameData game = getGame(gameID);
        GameData updatedGame = (playerColor.equals("WHITE")) ?
                new GameData(gameID, newUsername, game.blackUsername(), game.gameName(), game.game()) :
                new GameData(gameID, game.whiteUsername(), newUsername, game.gameName(), game.game());
        var statement = (playerColor.equals("WHITE")) ?
                "UPDATE game SET whiteUsername = ? WHERE gameID = ?;" :
                "UPDATE game SET blackUsername = ? WHERE gameID = ?;";
        executeUpdate(statement, newUsername, gameID);
        return updatedGame;
    }

    public void clearAllGames() throws DataAccessException {
        id = 0;
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        ChessGame game = new Gson().fromJson(rs.getString("gameData"), ChessGame.class);
        return new GameData(rs.getInt("gameID"),
                rs.getString("whiteUsername"), rs.getString("blackUsername"),
                rs.getString("gameName"), game);
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof int p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
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


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", ex.getMessage()));
        }
    }
}
