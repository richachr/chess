package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    public static ArrayList<GameData> mainArray = new ArrayList<>();

    public void createGame(GameData data) {
        mainArray.add(data);
    }

    public GameData getGame(int gameID) {
        for(GameData data : mainArray) {
            if(data.gameID() == gameID) {
                return data;
            }
        }
        return null;
    }

    public Collection<GameData> listGames() {
        return mainArray;
    }

    public void updateGame(GameData original, GameData replacement) throws DataAccessException {
        if(mainArray.contains(original)) {
            mainArray.set(mainArray.indexOf(original), replacement);
        } else {
            throw new DataAccessException("Game to update not found.");
        }
    }

    public void clear() {
        mainArray.clear();
    }
}
