package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

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
}
