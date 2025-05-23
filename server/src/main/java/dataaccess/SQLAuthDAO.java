package dataaccess;

public class SQLAuthDAO {
    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
}
