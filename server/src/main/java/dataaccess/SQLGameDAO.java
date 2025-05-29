package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String sql = """
                     CREATE TABLE IF NOT EXISTS game (
                     id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                     white_username VARCHAR(64),
                     black_username VARCHAR(64),
                     name VARCHAR(255) NOT NULL,
                     object_data TEXT NOT NULL,
                     FOREIGN KEY (white_username) REFERENCES user(username) ON UPDATE CASCADE ON DELETE CASCADE,
                     FOREIGN KEY (black_username) REFERENCES user(username) ON UPDATE CASCADE ON DELETE CASCADE
                     );
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public int createGame(GameData data) throws DataAccessException {
        String sql = """
                     INSERT INTO  game (white_username, black_username, name, object_data)
                     VALUES (?, ?, ?, ?);
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, data.whiteUsername());
            statement.setString(2, data.blackUsername());
            statement.setString(3, data.gameName());
            if(data.game() == null) {
                throw new DataAccessException("No game object provided.");
            }
            var gameDataText = new Gson().toJson(data.game());
            statement.setString(4, gameDataText);
            statement.executeUpdate();
            var rs = statement.getGeneratedKeys();
            if(rs.next()) {
                return rs.getInt(1);
            } else {
                throw new DataAccessException("No valid game ID generated, possible game creation failure.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String sql = """
                     SELECT * FROM game
                     WHERE id = ?;
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.setInt(1, gameID);
            var rs = statement.executeQuery();
            if(rs.next()) {
                ChessGame game = new Gson().fromJson(rs.getString("object_data"), ChessGame.class);
                return new GameData(rs.getInt("id"),
                                    rs.getString("white_username"),
                                    rs.getString("black_username"),
                                    rs.getString("name"),
                                    game);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList<>();
        String sql = """
                     SELECT * FROM game;
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            var rs = statement.executeQuery();
            while(rs.next()) {
                ChessGame game = new Gson().fromJson(rs.getString("object_data"), ChessGame.class);
                gameList.add(new GameData(rs.getInt("id"),
                                          rs.getString("white_username"),
                                          rs.getString("black_username"),
                                          rs.getString("name"),
                                          game)
                );
            }
            return gameList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public void updateGame(GameData original, GameData replacement) throws DataAccessException {
        String sql = """
                     SELECT * FROM game
                     WHERE id = ?;
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.setInt(1, original.gameID());
            var rs = statement.executeQuery();
            if(rs.next()) {
                sql = """
                     UPDATE game
                     SET white_username = ?, black_username = ?, name = ?, object_data = ?
                     WHERE id = ?;
                     """;
                try(var updateStatement = conn.prepareStatement(sql)) {
                    updateStatement.setString(1, replacement.whiteUsername());
                    updateStatement.setString(2, replacement.blackUsername());
                    updateStatement.setString(3, replacement.gameName());
                    if(replacement.game() == null) {
                        throw new DataAccessException("No game object provided.");
                    }
                    var gameDataText = new Gson().toJson(replacement.game());
                    updateStatement.setString(4, gameDataText);
                    updateStatement.setInt(5, original.gameID());
                    updateStatement.executeUpdate();
                }
            } else {
                throw new DataAccessException("Game to update not found.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM game;";
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
            sql = "ALTER TABLE game AUTO_INCREMENT = 1;";
            try(var resetStatement = conn.prepareStatement(sql)) {
                resetStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }
}
