package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();


}
