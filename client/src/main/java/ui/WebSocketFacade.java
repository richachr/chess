package ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facade.ResponseException;
import websocket.ClientData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageTypeAdapter;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        this.notificationHandler = notificationHandler;
        try {
            url = url.replace("http", "ws");
            URI socketAddress = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketAddress);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String s) {
                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ServerMessage.class, new ServerMessageTypeAdapter());
                    Gson gson = builder.create();

                    ServerMessage serverMessage = gson.fromJson(s, ServerMessage.class);
                    notificationHandler.notify(serverMessage);
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new ResponseException(500, e.getMessage());
        }

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect(ClientData data) throws ResponseException {
        var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, data);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
