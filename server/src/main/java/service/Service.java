package service;

import dataaccess.*;

import java.sql.SQLException;

public class Service {
    public static boolean isNotAuthorized(String authToken, boolean useMemoryDao) throws DataAccessException {
        AuthDAO auths;
        if(useMemoryDao) {
            auths = new MemoryAuthDAO();
        } else {
            auths = new SQLAuthDAO();
        }
        return auths.getAuth(authToken) == null;
    }
}
