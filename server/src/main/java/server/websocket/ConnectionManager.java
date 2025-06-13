package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(Session session, String username, Integer gameId) {
        Connection conn = new Connection(session, username);
        var gameConnections = connections.get(gameId);
        if(gameConnections == null) {
            gameConnections = new ArrayList<>();
        }
        gameConnections.add(conn);
        connections.put(gameId, gameConnections);
    }

    public void remove(String username, Integer gameId) {
        var gameConnections = connections.get(gameId);
        if(gameConnections == null) {
            return;
        }
        gameConnections.removeIf((conn) -> conn.username().equalsIgnoreCase(username));
        connections.put(gameId, gameConnections);
    }

    public void sendAndExclude(String userToExclude, Integer gameId, ServerMessage message) throws IOException {
        var gameConnections = connections.get(gameId);
        for(Connection conn : gameConnections) {
            if(conn.session().isOpen()) {
                if(!conn.username().equalsIgnoreCase(userToExclude)) {
                    conn.sendMessage(new Gson().toJson(message));
                }
            } else {
                gameConnections.remove(conn);
            }
        }
        connections.put(gameId, gameConnections);
    }

    public void sendMessageToUser(String user, Integer gameId, ServerMessage message) throws IOException {
        var gameConnections = connections.get(gameId);
        for(Connection conn : gameConnections) {
            if(conn.session().isOpen()) {
                if(conn.username().equalsIgnoreCase(user)) {
                    conn.sendMessage(new Gson().toJson(message));
                }
            } else {
                gameConnections.remove(conn);
            }
        }
        connections.put(gameId, gameConnections);
    }
}
