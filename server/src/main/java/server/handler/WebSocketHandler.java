package server.handler;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.SQLGameDAO;
import facade.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import server.websocket.ConnectionManager;
import service.UnauthorizedException;
import websocket.CoordinateHandler;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private static boolean USE_MEMORY_DAO = false;
    private final ConnectionManager connectionManager = new ConnectionManager();
    private GameDAO gameDAO;

    public WebSocketHandler(boolean useMemoryDao) throws ResponseException {
        this.USE_MEMORY_DAO = useMemoryDao;
        if(useMemoryDao) {
            gameDAO = new MemoryGameDAO();
        } else {
            try {
                gameDAO = new SQLGameDAO();
            } catch (DataAccessException e) {
                Server.handleException(e);
            }
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        try {
            switch(command.getCommandType()) {
                case CONNECT -> connect();
                case MAKE_MOVE -> makeMove();
                case LEAVE -> leave();
                case RESIGN -> resign();
            }
        } catch (Exception e) {
            Server.handleException();
        }
    }

    private void connect(Session session, String username, Integer gameId, ChessGame.TeamColor color) throws DataAccessException, IOException {
        connectionManager.add(session, username, gameId);
        ChessGame game = gameDAO.getGame(gameId).game();
        var loadGameMessage = new LoadGameMessage(game.getBoard());
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

    private void makeMove(Session session, String username, Integer gameId, ChessGame.TeamColor color, ChessMove move) throws DataAccessException, IOException, InvalidMoveException, UnauthorizedException {
        connectionManager.add(session, username, gameId);
        GameData data = gameDAO.getGame(gameId);
        if(color != data.game().getTeamTurn()) {
            throw new UnauthorizedException("It's not your turn.");
        }
        if(data.game().isComplete()) {
            throw new UnauthorizedException("The game is over. No moves can be played.");
        }
        data.game().makeMove(move);
        gameDAO.updateGame(gameId, data);
        var loadGameMessage = new LoadGameMessage(data.game().getBoard());
        connectionManager.sendAndExclude(null, gameId, loadGameMessage);
        String message = String.format("%s moved their piece at %s to %s", color.toString(),
                                       CoordinateHandler.getCoordinate(move.getStartPosition()),
                                       CoordinateHandler.getCoordinate(move.getEndPosition()));
        if(move.getPromotionPiece() != null) {
            message.concat(String.format(" and promoted to a %s", move.getPromotionPiece().toString().toLowerCase()));
        }
        var notification = new NotificationMessage(message);
        connectionManager.sendAndExclude(username, gameId, notification);
        if(data.game().isInCheck(color.getOpponent())) {
            message = String.format("%s is in check.", color.getOpponent());
        }
        if(data.game().isInStalemate(color.getOpponent())) {
            message = String.format("%s is in stalemate. The game is over.", color.getOpponent());
            data.game().setComplete(true);
            gameDAO.updateGame(gameId, data);
        }
        if(data.game().isInCheckmate(color.getOpponent())) {
            message = String.format("%s is in checkmate. The game is over.", color.getOpponent());
            data.game().setComplete(true);
            gameDAO.updateGame(gameId, data);
        }
        notification = new NotificationMessage(message);
        connectionManager.sendAndExclude(null, gameId, notification);
    }
}
