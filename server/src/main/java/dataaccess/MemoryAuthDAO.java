package dataaccess;

import model.AuthData;

import java.util.Arrays;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO {

    private static List<AuthData> mainArray;

    public void createAuth(AuthData data) {
        mainArray.add(data);
    }

    public AuthData getAuth(String authToken) {
        for(AuthData data : mainArray) {
            if(data.authToken() == authToken) {
                return data;
            }
        }
        return null;
    }

    public void deleteAuth(AuthData data) {
        mainArray.remove(data);
    }

    public void clear() {
        mainArray.clear();
    }
}
