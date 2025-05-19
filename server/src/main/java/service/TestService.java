package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class TestService {
    public void clear() {
        var users = new MemoryUserDAO();
        var auths = new MemoryAuthDAO();
        var games = new MemoryGameDAO();
        users.clear();
        auths.clear();
        games.clear();
    }
}
