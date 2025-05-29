package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String sql = """
                     CREATE TABLE IF NOT EXISTS auth (
                     username VARCHAR(64) NOT NULL,
                     auth_token CHAR(36) NOT NULL,
                     PRIMARY KEY (username, auth_token),
                     FOREIGN KEY (username) REFERENCES user(username) ON UPDATE CASCADE ON DELETE CASCADE
                     );
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public void createAuth(AuthData data) throws DataAccessException {
        String sql = """
                     INSERT INTO  auth (username, auth_token)
                     VALUES (?, ?);
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.setString(1, data.username());
            statement.setString(2, data.authToken());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        String sql = """
                     SELECT * FROM auth
                     WHERE auth_token = ?;
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.setString(1, authToken);
            var rs = statement.executeQuery();
            if(rs.next()) {
                return new AuthData(rs.getString("username"), rs.getString("auth_token"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public void deleteAuth(AuthData data) throws DataAccessException {
        String sql = """
                     DELETE FROM auth
                     WHERE username = ? AND auth_token = ?;
                     """;
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.setString(1, data.username());
            statement.setString(2, data.authToken());
            var rs = statement.executeUpdate();
            if(rs != 1) {
                throw new DataAccessException("No auth data found to delete.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM auth;";
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }
}
