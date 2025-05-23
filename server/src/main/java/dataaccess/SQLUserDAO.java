package dataaccess;

public class SQLUserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
}
