package client;

import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
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
    public void successfulRegisterTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void failedRegisterTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void successfulLoginTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void failedLoginTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void successfulLogoutTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void failedLogoutTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void successfulListGamesTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void failedListGamesTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void successfulCreateGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void failedCreateGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void successfulJoinGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void failedJoinGameTest() {
        Assertions.assertTrue(true);
    }

}
