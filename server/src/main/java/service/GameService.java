package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.ListGamesResult;

public class GameService {
    public static int nextGameID = 0;
    public ListGamesResult listGames(ListGamesRequest req) throws UnauthorizedException {
        var auths = new MemoryAuthDAO();
        if(auths.getAuth(req.authToken()) == null) {
            throw new UnauthorizedException();
        }
        var games = new MemoryGameDAO();
        return new ListGamesResult(games.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest req) throws BadRequestException, UnauthorizedException {
        if(req.gameName() == null ||
           req.authToken() == null) {
            throw new BadRequestException();
        }
        var auths = new MemoryAuthDAO();
        if(auths.getAuth(req.authToken()) == null) {
            throw new UnauthorizedException();
        }
        var games = new MemoryGameDAO();
        games.createGame(new GameData(++nextGameID, null, null, req.gameName(), new ChessGame()));
        return new CreateGameResult(nextGameID);
    }
}
