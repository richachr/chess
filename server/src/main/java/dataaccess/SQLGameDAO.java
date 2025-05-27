package dataaccess;

public class SQLGameDAO {

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
}
