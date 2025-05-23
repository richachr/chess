package dataaccess;

import javax.xml.crypto.Data;

public class SQLGameDAO {

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
}
