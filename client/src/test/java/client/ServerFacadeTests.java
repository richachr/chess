package client;

import model.GameData;
import org.junit.jupiter.api.*;
import request.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8008);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8008");
    }

    @BeforeEach
    public void clear() throws ResponseException {
        facade.sendRequest("/db", "DELETE", null, null);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void successfulRegisterTest() throws ResponseException {
        var res = facade.register(new RegisterRequest("user", "pass", "email"));
        Assertions.assertNotNull(res.authToken());
        Assertions.assertEquals("user", res.username());
    }

    @Test
    public void failedRegisterTest() {
        Assertions.assertThrows(ResponseException.class, () ->
                facade.register(new RegisterRequest(null, "good pass", "fake email")));
    }

    @Test
    public void successfulLoginTest() throws ResponseException {
        facade.register(new RegisterRequest("user", "pass", "email"));
        var res = facade.login(new LoginRequest("user", "pass"));
        Assertions.assertNotNull(res.authToken());
        Assertions.assertEquals("user", res.username());
    }

    @Test
    public void failedLoginTest() {
        Assertions.assertThrows(ResponseException.class, () ->
                facade.login(new LoginRequest("random blone", "pass")));
    }

    @Test
    public void successfulLogoutTest() throws ResponseException {
        var registerRes = facade.register(new RegisterRequest("user", "pass", "email"));
        facade.logout(new LogoutRequest(registerRes.authToken()));
        Assertions.assertThrows(ResponseException.class, () ->
                facade.logout(new LogoutRequest(registerRes.authToken())));
    }

    @Test
    public void failedLogoutTest() {
        Assertions.assertThrows(ResponseException.class, () ->
                facade.logout(new LogoutRequest("fake token")));
    }

    @Test
    public void successfulListGamesTest() throws ResponseException {
        var registerRes = facade.register(new RegisterRequest("user", "pass", "email"));
        var res = facade.listGames(new ListGamesRequest(registerRes.authToken()));
        Assertions.assertInstanceOf(GameData[].class, res.games());
        Assertions.assertEquals(0, res.games().length);
    }

    @Test
    public void failedListGamesTest() {
        Assertions.assertThrows(ResponseException.class, () ->
                facade.listGames(new ListGamesRequest("fake token")));
    }

    @Test
    public void successfulCreateGameTest() throws ResponseException {
        var registerRes = facade.register(new RegisterRequest("user", "pass", "email"));
        var res = facade.createGame(new CreateGameRequest("game", registerRes.authToken()));
        Assertions.assertNotNull(res.gameID());
    }

    @Test
    public void failedCreateGameTest() throws ResponseException {
        var registerRes = facade.register(new RegisterRequest("user", "pass", "email"));
        Assertions.assertThrows(ResponseException.class, () ->
                facade.createGame(new CreateGameRequest(null, registerRes.authToken())));
    }

    @Test
    public void successfulJoinGameTest() throws ResponseException {
        var registerRes = facade.register(new RegisterRequest("user", "pass", "email"));
        var createRes = facade.createGame(new CreateGameRequest("game", registerRes.authToken()));
        var beginningGamesList = facade.listGames(new ListGamesRequest(registerRes.authToken()));
        facade.joinGame(new JoinGameRequest(createRes.gameID(), "WHITE", registerRes.authToken()));
        Assertions.assertNotEquals(beginningGamesList, facade.listGames(new ListGamesRequest(registerRes.authToken())));
    }

    @Test
    public void failedJoinGameTest() throws ResponseException {
        var registerRes = facade.register(new RegisterRequest("user", "pass", "email"));
        var createRes = facade.createGame(new CreateGameRequest("game", registerRes.authToken()));
        Assertions.assertThrows(ResponseException.class, () ->
                facade.joinGame(new JoinGameRequest(createRes.gameID(), "blue", registerRes.authToken())));
    }

}
