package service;

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
        TestService.clear();
    }

    @Test
    public void clearTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"));
            GameService.createGame(new CreateGameRequest("test", userResponse.authToken()));
            Assertions.assertTrue(!MemoryAuthDAO.mainArray.isEmpty() && !MemoryGameDAO.mainArray.isEmpty() && !MemoryUserDAO.mainArray.isEmpty());
            TestService.clear();
            Assertions.assertTrue(MemoryAuthDAO.mainArray.isEmpty() && MemoryGameDAO.mainArray.isEmpty() && MemoryUserDAO.mainArray.isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulRegisterTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"));
            Assertions.assertTrue(userResponse.username().equals("user") && userResponse.authToken() != null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerBadRequestTest() {
        Assertions.assertThrows(BadRequestException.class,() -> UserService.register(new RegisterRequest(null,"password","email")));
        Assertions.assertThrows(BadRequestException.class,() -> UserService.register(new RegisterRequest("user",null,"email")));
        Assertions.assertThrows(BadRequestException.class,() -> UserService.register(new RegisterRequest("user","password",null)));
    }

    @Test
    public void registerAlreadyTakenTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"));
            Assertions.assertThrows(AlreadyTakenException.class, () -> UserService.register(new RegisterRequest("user", "password", "email")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulLoginTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"));
            var userResponse = UserService.login(new LoginRequest("user","password"));
            Assertions.assertTrue(userResponse.username().equals("user") && userResponse.authToken() != null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loginBadRequestTest() {
        Assertions.assertThrows(BadRequestException.class,() -> UserService.login(new LoginRequest(null,"password")));
        Assertions.assertThrows(BadRequestException.class,() -> UserService.login(new LoginRequest("user",null)));
    }

    @Test
    public void loginNotFoundTest() {
        Assertions.assertThrows(NotFoundException.class, () -> UserService.login(new LoginRequest("user", "password")));
    }

    @Test
    public void loginUnauthorizedTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(UnauthorizedException.class, () -> UserService.login(new LoginRequest("user", "wrongPassword")));
    }

    @Test
    public void successfulLogoutTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"));
            UserService.logout(new LogoutRequest(userResponse.authToken()));
            Assertions.assertTrue(MemoryAuthDAO.mainArray.isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void logoutBadRequestTest() {
        Assertions.assertThrows(BadRequestException.class,() -> UserService.logout(new LogoutRequest(null)));
    }

    @Test
    public void logoutNotFoundTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(NotFoundException.class, () -> UserService.logout(new LogoutRequest("this auth token does not exist in the database")));
        Assertions.assertFalse(MemoryAuthDAO.mainArray.isEmpty());
    }

    @Test
    public void listGamesEmptyTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"));
            Assertions.assertTrue(GameService.listGames(new ListGamesRequest(userResponse.authToken())).games().length == 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listGamesPopulatedTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"));
            GameService.createGame(new CreateGameRequest("game1", userResponse.authToken()));
            GameService.createGame(new CreateGameRequest("game2", userResponse.authToken()));
            GameService.createGame(new CreateGameRequest("game3", userResponse.authToken()));
            Assertions.assertEquals(3,GameService.listGames(new ListGamesRequest(userResponse.authToken())).games().length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listGamesUnauthorizedTest() {
        Assertions.assertThrows(UnauthorizedException.class, () -> GameService.createGame(new CreateGameRequest("game1", "fake auth token")));
    }

    @Test
    public void successfulCreateGameTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"));
            var gameResponse = GameService.createGame(new CreateGameRequest("game1", userResponse.authToken()));
            Assertions.assertTrue(gameResponse.gameID() != null && new MemoryGameDAO().getGame(gameResponse.gameID()) != null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameBadRequestTest() {
        RegisterResult userResponse;
        try {
            userResponse = UserService.register(new RegisterRequest("user","password","email"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RegisterResult finalUserResponse = userResponse;
        Assertions.assertThrows(BadRequestException.class, () -> GameService.createGame(new CreateGameRequest(null, finalUserResponse.authToken())));
        Assertions.assertThrows(BadRequestException.class, () -> GameService.createGame(new CreateGameRequest("game1", null)));
    }

    @Test
    public void createGameUnauthorizedTest() {
        Assertions.assertThrows(UnauthorizedException.class, () -> GameService.createGame(new CreateGameRequest("game name", "another fake auth token")));
    }

    @Test
    public void successfulJoinGameTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("username1","password","email"));
            var gameResponse = GameService.createGame(new CreateGameRequest("game1", userResponse.authToken()));
            GameService.joinGame(new JoinGameRequest(gameResponse.gameID(), "WHITE", userResponse.authToken()));
            Assertions.assertSame("username1", new MemoryGameDAO().getGame(gameResponse.gameID()).whiteUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void joinGameBadRequestTest() {
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(new JoinGameRequest(null, "WHITE", "token")));
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(new JoinGameRequest(0, null, "token")));
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(new JoinGameRequest(0, "WHITE", null)));
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(new JoinGameRequest(0, "green", "token")));
    }

    @Test
    public void joinGameUnauthorizedTest() {
        Assertions.assertThrows(UnauthorizedException.class, () -> GameService.joinGame(new JoinGameRequest(0, "WHITE", "token")));
    }

    @Test
    public void joinGameNotFoundTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("username1","password","email"));
            Assertions.assertThrows(NotFoundException.class, () -> GameService.joinGame(new JoinGameRequest(26, "WHITE", userResponse.authToken())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void joinGameAlreadyTakenTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("username1","password","email"));
            var gameResponse = GameService.createGame(new CreateGameRequest("game1", userResponse.authToken()));
            GameService.joinGame(new JoinGameRequest(gameResponse.gameID(), "WHITE", userResponse.authToken()));
            Assertions.assertThrows(AlreadyTakenException.class, () -> GameService.joinGame(new JoinGameRequest(gameResponse.gameID(), "WHITE", userResponse.authToken())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
