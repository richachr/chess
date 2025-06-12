package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public record Connection(Session session, String username) {
    public void sendMessage(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
