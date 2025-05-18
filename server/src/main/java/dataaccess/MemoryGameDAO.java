package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    private static List<GameData> mainArray;

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

    public void updateGame(GameData original, GameData replacement) {
        mainArray.set(mainArray.indexOf(original), replacement);
    }

    public void clear() {
        mainArray.clear();
    }
}
