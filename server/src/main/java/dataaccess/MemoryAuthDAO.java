package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {

    public static ArrayList<AuthData> mainArray = new ArrayList<>();

    public void createAuth(AuthData data) {
        mainArray.add(data);
    }

    public AuthData getAuth(String authToken) {
        for(AuthData data : mainArray) {
            if(authToken.equals(data.authToken())) {
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
