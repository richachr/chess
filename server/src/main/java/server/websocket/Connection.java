package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public record Connection(Session session, String authToken) {
    public void sendMessage(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
