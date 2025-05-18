package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO extends DataAccessObject {

    void createGame(GameData data);

    GameData getGame(int gameID);

    Collection<GameData> listGames();

    void updateGame(GameData data);


}
