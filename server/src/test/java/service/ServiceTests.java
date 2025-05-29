package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.RegisterResult;

public class ServiceTests {

    @BeforeEach
    public void clearData() {
        try {
            TestService.clear(true);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"), true);
            GameService.createGame(new CreateGameRequest("test", userResponse.authToken()), true);
            Assertions.assertTrue(!MemoryAuthDAO.mainArray.isEmpty() &&
                                           !MemoryGameDAO.mainArray.isEmpty() &&
                                           !MemoryUserDAO.mainArray.isEmpty());
            TestService.clear(true);
            Assertions.assertTrue(MemoryAuthDAO.mainArray.isEmpty() &&
                                           MemoryGameDAO.mainArray.isEmpty() &&
                                           MemoryUserDAO.mainArray.isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulRegisterTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"), true);
            Assertions.assertTrue(userResponse.username().equals("user") && userResponse.authToken() != null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerBadRequestTest() {
        Assertions.assertThrows(BadRequestException.class,() ->
                                UserService.register(new RegisterRequest(null,"password","email"), true));
        Assertions.assertThrows(BadRequestException.class,() ->
                                UserService.register(new RegisterRequest("user",null,"email"), true));
        Assertions.assertThrows(BadRequestException.class,() ->
                                UserService.register(new RegisterRequest("user","password",null), true));
    }

    @Test
    public void registerAlreadyTakenTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"), true);
            Assertions.assertThrows(AlreadyTakenException.class, () ->
                                    UserService.register(new RegisterRequest("user", "password", "email"), true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulLoginTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"), true);
            var userResponse = UserService.login(new LoginRequest("user","password"), true);
            Assertions.assertTrue(userResponse.username().equals("user") && userResponse.authToken() != null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loginBadRequestTest() {
        Assertions.assertThrows(BadRequestException.class,() -> UserService.login(new LoginRequest(null,"password"), true));
        Assertions.assertThrows(BadRequestException.class,() -> UserService.login(new LoginRequest("user",null), true));
    }

    @Test
    public void loginNotFoundTest() {
        Assertions.assertThrows(NotFoundException.class, () -> UserService.login(new LoginRequest("user", "password"), true));
    }

    @Test
    public void loginUnauthorizedTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(UnauthorizedException.class, () -> UserService.login(new LoginRequest("user", "wrongPassword"), true));
    }

    @Test
    public void successfulLogoutTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"), true);
            UserService.logout(new LogoutRequest(userResponse.authToken()), true);
            Assertions.assertTrue(MemoryAuthDAO.mainArray.isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void logoutBadRequestTest() {
        Assertions.assertThrows(BadRequestException.class,() -> UserService.logout(new LogoutRequest(null), true));
    }

    @Test
    public void logoutNotFoundTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(NotFoundException.class, () ->
                                UserService.logout(new LogoutRequest("this auth token does not exist in the database"), true));
        Assertions.assertFalse(MemoryAuthDAO.mainArray.isEmpty());
    }

    @Test
    public void listGamesEmptyTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"), true);
            Assertions.assertEquals(0, GameService.listGames(new ListGamesRequest(userResponse.authToken()), true).games().length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listGamesPopulatedTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"), true);
            GameService.createGame(new CreateGameRequest("game1", userResponse.authToken()), true);
            GameService.createGame(new CreateGameRequest("game2", userResponse.authToken()), true);
            GameService.createGame(new CreateGameRequest("game3", userResponse.authToken()), true);
            Assertions.assertEquals(3,GameService.listGames(new ListGamesRequest(userResponse.authToken()), true).games().length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listGamesUnauthorizedTest() {
        Assertions.assertThrows(UnauthorizedException.class, () ->
                                GameService.createGame(new CreateGameRequest("game1", "fake auth token"), true));
    }

    @Test
    public void successfulCreateGameTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"), true);
            var gameResponse = GameService.createGame(new CreateGameRequest("game1", userResponse.authToken()), true);
            Assertions.assertTrue(gameResponse.gameID() != null && new MemoryGameDAO().getGame(gameResponse.gameID()) != null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameBadRequestTest() {
        RegisterResult userResponse;
        try {
            userResponse = UserService.register(new RegisterRequest("user","password","email"), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RegisterResult finalUserResponse = userResponse;
        Assertions.assertThrows(BadRequestException.class, () ->
                                GameService.createGame(new CreateGameRequest(null, finalUserResponse.authToken()), true));
        Assertions.assertThrows(BadRequestException.class, () ->
                                GameService.createGame(new CreateGameRequest("game1", null), true));
    }

    @Test
    public void createGameUnauthorizedTest() {
        Assertions.assertThrows(UnauthorizedException.class, () ->
                                GameService.createGame(new CreateGameRequest("game name", "another fake auth token"), true));
    }

    @Test
    public void successfulJoinGameTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("username1","password","email"), true);
            var gameResponse = GameService.createGame(new CreateGameRequest("game1", userResponse.authToken()), true);
            GameService.joinGame(new JoinGameRequest(gameResponse.gameID(), "WHITE", userResponse.authToken()), true);
            Assertions.assertSame("username1", new MemoryGameDAO().getGame(gameResponse.gameID()).whiteUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void joinGameBadRequestTest() {
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(new JoinGameRequest(null, "WHITE", "token"), true));
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(new JoinGameRequest(0, null, "token"), true));
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(new JoinGameRequest(0, "WHITE", null), true));
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(new JoinGameRequest(0, "green", "token"), true));
    }

    @Test
    public void joinGameUnauthorizedTest() {
        Assertions.assertThrows(UnauthorizedException.class, () -> GameService.joinGame(new JoinGameRequest(0, "WHITE", "token"), true));
    }

    @Test
    public void joinGameNotFoundTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("username1","password","email"), true);
            Assertions.assertThrows(NotFoundException.class, () ->
                    GameService.joinGame(new JoinGameRequest(26, "WHITE", userResponse.authToken()), true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void joinGameAlreadyTakenTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("username1","password","email"), true);
            var gameResponse = GameService.createGame(new CreateGameRequest("game1", userResponse.authToken()), true);
            GameService.joinGame(new JoinGameRequest(gameResponse.gameID(), "WHITE", userResponse.authToken()), true);
            Assertions.assertThrows(AlreadyTakenException.class, () ->
                                    GameService.joinGame(new JoinGameRequest(gameResponse.gameID(), "WHITE", userResponse.authToken()), true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
