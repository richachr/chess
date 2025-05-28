package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String sql = """
                     CREATE TABLE IF NOT EXISTS user (
                     username VARCHAR(64) PRIMARY KEY NOT NULL,
                     password VARCHAR(255) NOT NULL,
                     email VARCHAR(255) NOT NULL
                     );
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public void createUser(UserData data) throws DataAccessException {
        String sql = """
                     INSERT INTO  user (username, password, email)
                     VALUES (?, ?, ?);
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.setString(1, data.username());
            statement.setString(2, data.password());
            statement.setString(3, data.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        String sql = """
                     SELECT * FROM user
                     WHERE username = ?;
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            var rs = statement.executeQuery();
            if(rs.next()) {
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM user;";
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }
}
