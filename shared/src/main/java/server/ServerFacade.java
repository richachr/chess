package server;

import request.*;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginResult;
import result.RegisterResult;

public class ServerFacade {
    public String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    public RegisterResult register(RegisterRequest req) {
        this.sendRequest("/user", "POST", req, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) {
        this.sendRequest("/user", "POST", req, RegisterResult.class);
    }

    public void logout(LogoutRequest req) {
        this.sendRequest("/user", "POST", req, RegisterResult.class);
    }

    public ListGamesResult listGames(ListGamesRequest req) {
        this.sendRequest("/user", "POST", req, RegisterResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        this.sendRequest("/user", "POST", req, RegisterResult.class);
    }

    public void joinGame(JoinGameRequest req) {
        this.sendRequest("/user", "POST", req, RegisterResult.class);
    }


}
