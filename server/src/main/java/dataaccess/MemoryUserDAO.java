package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    public static ArrayList<UserData> mainArray = new ArrayList<>();

    public void createUser(UserData data) {
        mainArray.add(data);
    }

    public UserData getUser(String username) {
        for(UserData data : mainArray) {
            if(username.equals(data.username())) {
                return data;
            }
        }
        return null;
    }

    public void clear() {
        mainArray.clear();
    }
}
