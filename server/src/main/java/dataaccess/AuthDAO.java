package dataaccess;

import model.AuthData;

public interface AuthDAO extends DataAccessObject {

    void createAuth(AuthData data) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(AuthData data) throws DataAccessException;
}
