package server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.websocket.ConnectionManager;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    private static boolean USE_MEMORY_DAO = false;
    private final ConnectionManager connectionManager = new ConnectionManager();

    public WebSocketHandler(boolean useMemoryDao) {
        this.USE_MEMORY_DAO = useMemoryDao;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()) {
            case CONNECT -> connect();
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }
}
