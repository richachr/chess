package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.ListGamesResult;

public class GameService extends Service {
    public static int nextGameID = 0;

    public static ListGamesResult listGames(ListGamesRequest req) throws UnauthorizedException {
        if(Service.isNotAuthorized(req.authToken())) {
            throw new UnauthorizedException();
        }
        var games = new MemoryGameDAO();
        return new ListGamesResult(games.listGames().toArray(GameData[]::new));
    }

    public static CreateGameResult createGame(CreateGameRequest req) throws BadRequestException, UnauthorizedException {
        if(req.gameName() == null ||
           req.authToken() == null) {
            throw new BadRequestException();
        }
        if(Service.isNotAuthorized(req.authToken())) {
            throw new UnauthorizedException();
        }
        var games = new MemoryGameDAO();
        games.createGame(new GameData(++nextGameID, null, null, req.gameName(), new ChessGame()));
        return new CreateGameResult(nextGameID);
    }

    public static void joinGame(JoinGameRequest req) throws BadRequestException, UnauthorizedException, NotFoundException, AlreadyTakenException {
        if(req.gameID() == null ||
           req.authToken() == null ||
           req.playerColor() == null || (!req.playerColor().equalsIgnoreCase("WHITE") && !req.playerColor().equalsIgnoreCase("BLACK"))) {
            throw new BadRequestException();
        }
        var auths = new MemoryAuthDAO();
        var authData = auths.getAuth(req.authToken());
        if(authData == null) {
            throw new UnauthorizedException();
        }
        var games = new MemoryGameDAO();
        var gameData = games.getGame(req.gameID());
        if(gameData == null) {
            throw new NotFoundException();
        }
        if(gameData.isTaken(req.playerColor())) {
            throw new AlreadyTakenException();
        } else {
            GameData newGameData = getNewGameData(req, gameData, authData);
            try {
                games.updateGame(gameData,newGameData);
            } catch (DataAccessException e) {
                throw new NotFoundException(e.getLocalizedMessage());
            }
        }
    }

    private static GameData getNewGameData(JoinGameRequest req, GameData gameData, AuthData authData) {
        GameData newGameData;
        switch(req.playerColor().toUpperCase()) {
            case "WHITE" -> newGameData = new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game());
            case "BLACK" -> newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
            default -> throw new RuntimeException("Unexpected team color.");
        }
        return newGameData;
    }
}
