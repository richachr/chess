package server.websocket;

import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageTypeAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public static final ConcurrentHashMap<Integer, ArrayList<Connection>> CONNECTIONS = new ConcurrentHashMap<>();

    public void add(Session session, String authToken, Integer gameId) {
        Connection conn = new Connection(session, authToken);
        var gameConnections = CONNECTIONS.get(gameId);
        if(gameConnections == null) {
            gameConnections = new ArrayList<>();
        }
        gameConnections.add(conn);
        CONNECTIONS.put(gameId, gameConnections);
    }

    public void remove(String authToken, Integer gameId) {
        var gameConnections = CONNECTIONS.get(gameId);
        if(gameConnections == null) {
            return;
        }
        gameConnections.removeIf((conn) -> conn.authToken().equalsIgnoreCase(authToken));
        CONNECTIONS.put(gameId, gameConnections);
    }

    public void sendAndExclude(String authToExclude, Integer gameId, ServerMessage message) throws IOException {
        var gameConnections = CONNECTIONS.get(gameId);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServerMessage.class, new ServerMessageTypeAdapter());
        var gson = builder.create();
        String messageString = gson.toJson(message);
        for(Connection conn : gameConnections) {
            if(conn.session().isOpen()) {
                if(!conn.authToken().equalsIgnoreCase(authToExclude)) {
                    conn.sendMessage(messageString);
                }
            } else {
                gameConnections.remove(conn);
            }
        }
        CONNECTIONS.put(gameId, gameConnections);
    }

    public void sendMessageToUser(String authToken, Integer gameId, ServerMessage message) throws IOException {
        var gameConnections = CONNECTIONS.get(gameId);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServerMessage.class, new ServerMessageTypeAdapter());
        var gson = builder.create();
        String messageString = gson.toJson(message);
        for(Connection conn : gameConnections) {
            if(conn.session().isOpen()) {
                if(conn.authToken().equalsIgnoreCase(authToken)) {
                    conn.sendMessage(messageString);
                }
            } else {
                gameConnections.remove(conn);
            }
        }
        CONNECTIONS.put(gameId, gameConnections);
    }
}
