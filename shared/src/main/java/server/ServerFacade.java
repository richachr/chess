package server;

import request.*;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginResult;
import result.RegisterResult;

import java.io.IOException;
import java.net.*;

public class ServerFacade {
    public String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    public RegisterResult register(RegisterRequest req) {
        return this.sendRequest("/user", "POST", req, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) {
        return this.sendRequest("/session", "POST", req, LoginResult.class);
    }

    public void logout(LogoutRequest req) {
        this.sendRequest("/session", "DELETE", req, null);
    }

    public ListGamesResult listGames(ListGamesRequest req) {
        return this.sendRequest("/game", "GET", req, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        return this.sendRequest("/game", "POST", req, CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest req) {
        this.sendRequest("/game", "PUT", req, null);
    }

    public <T> T sendRequest(String path, String method, Object request, Class<T> responseClass) {
        try {
            HttpURLConnection connection = getConnection(path);
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            setBody(connection, request);
            connection.connect();
            if(connection.getResponseCode() != 200) {
                handleErrors(connection);
            }
            return getBody(connection, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpURLConnection getConnection(String path) {
        try {
            var requestUrl = new URI(this.url + path).toURL();
            return (HttpURLConnection) requestUrl.openConnection();
        }  catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}
