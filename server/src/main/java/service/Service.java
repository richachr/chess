package service;

import dataaccess.MemoryAuthDAO;

public class Service {
    public static boolean isAuthorized(String authToken) {
        var auths = new MemoryAuthDAO();
        return auths.getAuth(authToken) == null;
    }
}
