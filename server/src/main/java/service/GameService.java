package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.ListGamesResult;

public class GameService extends Service {

    public static ListGamesResult listGames(ListGamesRequest req, boolean useMemoryDao) throws UnauthorizedException, DataAccessException {
        if(Service.isNotAuthorized(req.authToken(), useMemoryDao)) {
            throw new UnauthorizedException("Unauthorized; please log in first.");
        }
        GameDAO games;
        if(useMemoryDao) {
            games = new MemoryGameDAO();
        } else {
            games = new SQLGameDAO();
        }
        return new ListGamesResult(games.listGames().toArray(GameData[]::new));
    }

    public static CreateGameResult createGame(CreateGameRequest req, boolean useMemoryDao)
            throws BadRequestException, UnauthorizedException, DataAccessException {
        if(req.gameName() == null ||
           req.authToken() == null) {
            throw new BadRequestException("Bad request: incomplete data.");
        }
        if(Service.isNotAuthorized(req.authToken(), useMemoryDao)) {
            throw new UnauthorizedException("Unauthorized; please log in first.");
        }
        GameDAO games;
        if(useMemoryDao) {
            games = new MemoryGameDAO();
        } else {
            games = new SQLGameDAO();
        }
        int gameId = games.createGame(new GameData(null, null, null, req.gameName(), new ChessGame()));
        return new CreateGameResult(gameId);
    }

    public static void joinGame(JoinGameRequest req, boolean useMemoryDao)
            throws BadRequestException, UnauthorizedException, NotFoundException, AlreadyTakenException, DataAccessException {
        if(req.gameID() == null ||
           req.authToken() == null ||
           req.playerColor() == null) {
            throw new BadRequestException("Bad request: incomplete data.");
        }
        if(!req.playerColor().equalsIgnoreCase("WHITE") && !req.playerColor().equalsIgnoreCase("BLACK")) {
            throw new BadRequestException("Bad request: invalid team color.");
        }
        GameDAO games;
        AuthDAO auths;
        if(useMemoryDao) {
            games = new MemoryGameDAO();
            auths = new MemoryAuthDAO();
        } else {
            games = new SQLGameDAO();
            auths = new SQLAuthDAO();
        }
        var authData = auths.getAuth(req.authToken());
        if(authData == null) {
            throw new UnauthorizedException("Unauthorized; please log in first.");
        }
        var gameData = games.getGame(req.gameID());
        if(gameData == null) {
            throw new NotFoundException("No game found with that ID.");
        }
        if(gameData.isTaken(req.playerColor())) {
            throw new AlreadyTakenException("That color is already taken.");
        } else {
            GameData newGameData = getNewGameData(req, gameData, authData);
            try {
                games.updateGame(gameData.gameID(), newGameData);
            } catch (DataAccessException e) {
                throw new NotFoundException(e.getLocalizedMessage());
            }
        }
    }

    private static GameData getNewGameData(JoinGameRequest req, GameData gameData, AuthData authData) {
        GameData newGameData;
        switch(req.playerColor().toUpperCase()) {
            case "WHITE" -> newGameData = new GameData(gameData.gameID(),
                                                       authData.username(),
                                                       gameData.blackUsername(),
                                                       gameData.gameName(),
                                                       gameData.game());

            case "BLACK" -> newGameData = new GameData(gameData.gameID(),
                                                       gameData.whiteUsername(),
                                                       authData.username(),
                                                       gameData.gameName(),
                                                       gameData.game());
            default -> throw new RuntimeException("Unexpected team color.");
        }
        return newGameData;
    }
}
