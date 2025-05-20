package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class TestService extends Service {
    public void clear() {
        var users = new MemoryUserDAO();
        var auths = new MemoryAuthDAO();
        var games = new MemoryGameDAO();
        users.clear();
        auths.clear();
        games.clear();
    }
}
