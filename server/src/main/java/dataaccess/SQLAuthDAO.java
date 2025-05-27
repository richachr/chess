package dataaccess;

import java.sql.SQLException;

public class SQLAuthDAO {
    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
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
