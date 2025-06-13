package server.handler;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import facade.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.websocket.ConnectionManager;
import service.BadRequestException;
import service.Service;
import service.UnauthorizedException;
import websocket.CoordinateHandler;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final boolean useMemoryDao;
    private final ConnectionManager connectionManager = new ConnectionManager();
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public WebSocketHandler() throws ResponseException {
        this(false);
    }

    public WebSocketHandler(boolean useMemoryDao) throws ResponseException {
        this.useMemoryDao = useMemoryDao;
        if(useMemoryDao) {
            gameDAO = new MemoryGameDAO();
            authDAO = new MemoryAuthDAO();
        } else {
            try {
                gameDAO = new SQLGameDAO();
                authDAO = new SQLAuthDAO();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        try {
            switch(command.getCommandType()) {
                case CONNECT -> connect(session, command.getAuthToken(), command.getGameID());
                case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getMove());
                case LEAVE -> leave(command.getAuthToken(), command.getGameID());
                case RESIGN -> resign(command.getAuthToken(), command.getGameID());
            }
        } catch (Exception e) {
            handleError(session, e);
        }
    }

    @OnWebSocketError
    public void handleError(Session session, Throwable e) {
        var errorMessage = new ErrorMessage(e.getMessage());
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServerMessage.class, new ServerMessageTypeAdapter());
        var gson = builder.create();
        String messageString = gson.toJson(errorMessage);
        try {
            session.getRemote().sendString(messageString);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void connect(Session session, String authToken, Integer gameId)
            throws DataAccessException, IOException, BadRequestException, UnauthorizedException {
        String username = getUsernameAndCheckAuth(authToken);
        connectionManager.add(session, username, gameId);
        GameData data = gameDAO.getGame(gameId);
        if(data == null) {
            throw new BadRequestException("Game ID not found.");
        }
        ChessGame.TeamColor color = getColor(username, data);
        var loadGameMessage = new LoadGameMessage(data.game());
        connectionManager.sendMessageToUser(username, gameId, loadGameMessage);
        String message;
        if(color == null) {
            message = String.format("%s is observing.", username);
        } else {
            message = String.format("%s is playing as %s", username, color.toString().toLowerCase());
        }
        var notification = new NotificationMessage(message);
        connectionManager.sendAndExclude(username, gameId, notification);
    }

    private void makeMove(String authToken, Integer gameId, ChessMove move)
            throws DataAccessException, IOException, InvalidMoveException, UnauthorizedException, BadRequestException {
        String username = getUsernameAndCheckAuth(authToken);
        GameData data = gameDAO.getGame(gameId);
        if(data == null) {
            throw new BadRequestException("Game ID not found.");
        }
        if(Service.isNotAuthorized(authToken, useMemoryDao)) {
            throw new UnauthorizedException("Invalid authentication. Please log in again.");
        }
        ChessGame.TeamColor color = getColor(username, data);
        if(color == null) {
            throw new UnauthorizedException("Observers can't make moves.");
        }
        if(color != data.game().getTeamTurn()) {
            throw new UnauthorizedException("It's not your turn.");
        }
        if(data.game().isComplete()) {
            throw new UnauthorizedException("The game is over. No moves can be played.");
        }
        data.game().makeMove(move);
        gameDAO.updateGame(gameId, data);
        var loadGameMessage = new LoadGameMessage(data.game());
        connectionManager.sendAndExclude(null, gameId, loadGameMessage);
        String message = String.format("%s moved their piece at %s to %s", username,
                                       CoordinateHandler.getCoordinate(move.getStartPosition()),
                                       CoordinateHandler.getCoordinate(move.getEndPosition()));
        if(move.getPromotionPiece() != null) {
            message = message.concat(String.format(" and promoted to a %s", move.getPromotionPiece().toString().toLowerCase()));
        }
        var notification = new NotificationMessage(message);
        connectionManager.sendAndExclude(username, gameId, notification);
        String opponentUsername = null;
        switch(color.getOpponent()) {
            case WHITE -> opponentUsername = data.whiteUsername();
            case BLACK -> opponentUsername = data.blackUsername();
        }
        if(data.game().isInCheck(color.getOpponent())) {
            message = String.format("%s is in check.", opponentUsername);
        }
        if(data.game().isInStalemate(color.getOpponent())) {
            message = String.format("%s is in stalemate. The game is over.", opponentUsername);
            data.game().setComplete(true);
            gameDAO.updateGame(gameId, data);
        }
        if(data.game().isInCheckmate(color.getOpponent())) {
            message = String.format("%s is in checkmate. The game is over.", opponentUsername);
            data.game().setComplete(true);
            gameDAO.updateGame(gameId, data);
        }
        if(data.game().isInCheck(color.getOpponent()) ||
                data.game().isInStalemate(color.getOpponent()) ||
                data.game().isInCheckmate(color.getOpponent())) {
            notification = new NotificationMessage(message);
            connectionManager.sendAndExclude(null, gameId, notification);
        }
    }

    private void leave(String authToken, Integer gameId)
            throws IOException, DataAccessException, UnauthorizedException {
        String username = getUsernameAndCheckAuth(authToken);
        GameData game = gameDAO.getGame(gameId);
        ChessGame.TeamColor color = getColor(username, game);
        if(color != null) {
            switch(color) {
                case WHITE ->
                    game = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
                case BLACK ->
                    game = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
            }
            gameDAO.updateGame(gameId, game);
        }
        String message = String.format("%s left the game.", username);
        var notification = new NotificationMessage(message);
        connectionManager.sendAndExclude(username, gameId, notification);
        connectionManager.remove(username, gameId);
    }

    private void resign(String authToken, Integer gameId)
            throws IOException, DataAccessException, UnauthorizedException, BadRequestException {
        String username = getUsernameAndCheckAuth(authToken);
        GameData game = gameDAO.getGame(gameId);
        if(game.game().isComplete()) {
            throw new BadRequestException("The game is already over.");
        }
        ChessGame.TeamColor color = getColor(username, game);
        switch(color) {
            case WHITE, BLACK -> game.game().setComplete(true);
            case null -> throw new UnauthorizedException("Observers cannot resign. Use `leave` instead.");
        }
        gameDAO.updateGame(gameId, game);
        String message = String.format("%s resigned. The game is over.", username);
        var notification = new NotificationMessage(message);
        connectionManager.sendAndExclude(null, gameId, notification);
    }

    private String getUsernameAndCheckAuth(String authToken) throws UnauthorizedException, DataAccessException {
        var authData = authDAO.getAuth(authToken);
        if(authData == null) {
            throw new UnauthorizedException("Invalid authentication. Please log in again.");
        }
        return authData.username();
    }

    private ChessGame.TeamColor getColor(String username, GameData game) {
        if(username.equalsIgnoreCase(game.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        } else if(username.equalsIgnoreCase(game.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else {
            return null;
        }
    }
}
