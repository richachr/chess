package service;

import dataaccess.*;

public class TestService extends Service {
    public static void clear(boolean useMemoryDao) throws DataAccessException {
        GameDAO games;
        AuthDAO auths;
        UserDAO users;
        if(useMemoryDao) {
            users = new MemoryUserDAO();
            auths = new MemoryAuthDAO();
            games = new MemoryGameDAO();
        } else {
            users = new SQLUserDAO();
            auths = new SQLAuthDAO();
            games = new SQLGameDAO();
        }

        users.clear();
        auths.clear();
        games.clear();
    }
}
