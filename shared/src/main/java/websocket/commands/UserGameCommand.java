package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;
import websocket.ClientData;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    private final ChessGame.TeamColor color;

    private final ChessMove move;

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = null;
        this.move = null;
    }

    // May need type adapter to convert parameters to TeamColor and ChessMove objects.

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor color) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;
        this.move = null;
    }

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor color, ChessMove move) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;
        this.move = move;
    }

    public UserGameCommand(CommandType commandType, ClientData data) {
        this.commandType = commandType;
        this.authToken = data.authToken();
        this.color = data.playerColor();
        this.gameID = data.gameId();
        this.move = null;
    }

    public UserGameCommand(CommandType commandType, ClientData data, ChessMove move) {
        this.commandType = commandType;
        this.authToken = data.authToken();
        this.color = data.playerColor();
        this.gameID = data.gameId();
        this.move = move;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand)) {
            return false;
        }
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}
