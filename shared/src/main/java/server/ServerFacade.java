package server;

import com.google.gson.Gson;
import request.*;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginResult;
import result.RegisterResult;

import java.io.*;
import java.net.*;

public class ServerFacade {
    public String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        return this.sendRequest("/user", "POST", req, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        return this.sendRequest("/session", "POST", req, LoginResult.class);
    }

    public void logout(LogoutRequest req) throws ResponseException {
        this.sendRequest("/session", "DELETE", req, null);
    }

    public ListGamesResult listGames(ListGamesRequest req) throws ResponseException {
        return this.sendRequest("/game", "GET", req, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ResponseException {
        return this.sendRequest("/game", "POST", req, CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest req) throws ResponseException {
        this.sendRequest("/game", "PUT", req, null);
    }

    public <T> T sendRequest(String path, String method, Object request, Class<T> responseClass) throws ResponseException {
        try {
            HttpURLConnection connection = getConnection(path);
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            setReqBody(connection, request);
            connection.connect();
            if(connection.getResponseCode() != 200) {
                handleErrors(connection);
            }
            return getResBody(connection, responseClass);
        } catch (ResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseException(500, e.getLocalizedMessage());
        }
    }

    public HttpURLConnection getConnection(String path) throws IOException {
        try {
            var requestUrl = new URI(this.url + path).toURL();
            return (HttpURLConnection) requestUrl.openConnection();
        }  catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void setReqBody(HttpURLConnection connection, Object request) throws IOException {
        if(request != null) {
            connection.addRequestProperty("Content-Type", "application/json");
            String requestText = new Gson().toJson(request);
            try(OutputStream out = connection.getOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(out)) {
                writer.write(requestText);
            }
        }
    }

    public <T> T getResBody(HttpURLConnection connection, Class<T> responseClass) throws IOException {
        T response = null;
        if(responseClass != null && connection.getContentLength() != 0) {
            try(InputStream resBody = connection.getInputStream(); InputStreamReader reader = new InputStreamReader(resBody)) {
                response = new Gson().fromJson(reader, responseClass);
            }
        }
        return response;
    }

    public void handleErrors(HttpURLConnection connection) throws IOException, ResponseException {
        int statusCode = connection.getResponseCode();
        try(InputStream in = connection.getErrorStream();
            InputStreamReader inputReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inputReader)) {
            String line;
            StringBuilder errorMsg = new StringBuilder();
            while((line = reader.readLine()) != null) {
                errorMsg.append(line);
            }
            throw new ResponseException(statusCode, errorMsg.toString());
        }
    }

}
