package dataaccess;

import java.sql.SQLException;

public class SQLGameDAO {

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM game;";
        try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }
}
