package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO extends DataAccessObject {

    int createGame(GameData data) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(Integer originalGameId, GameData replacement) throws DataAccessException;


}
