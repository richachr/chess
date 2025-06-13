package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    public static int nextGameID = 0;
    public static ArrayList<GameData> mainArray = new ArrayList<>();

    public int createGame(GameData data) {
        mainArray.add(new GameData(++nextGameID, data.whiteUsername(), data.blackUsername(), data.gameName(), data.game()));
        return nextGameID;
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

    public void updateGame(Integer originalGameId, GameData replacement) throws DataAccessException {
        for(var game : mainArray) {
            if(game.gameID().equals(originalGameId)) {
                mainArray.set(mainArray.indexOf(game), replacement);
                return;
            }
        }
        throw new DataAccessException("Game to update not found.");
    }

    public void clear() {
        mainArray.clear();
    }
}
