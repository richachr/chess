package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.RegisterRequest;
import service.GameService;
import service.TestService;
import service.UserService;

import java.sql.SQLException;
import java.util.UUID;


public class DAOTests {

    @BeforeEach
    public void clearData() {
        try {
            TestService.clear(false);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearUsersTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"), false);
            String sql = "SELECT * FROM user;";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertTrue(rs.next());
            }
            new SQLUserDAO().clear();
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertFalse(rs.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearGamesTest() {
        try {
            var userResponse = UserService.register(new RegisterRequest("user","password","email"), false);
            GameService.createGame(new CreateGameRequest("test", userResponse.authToken()), false);
            String sql = "SELECT * FROM game;";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertTrue(rs.next());
            }
            new SQLGameDAO().clear();
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertFalse(rs.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearAuthTest() {
        try {
            UserService.register(new RegisterRequest("user","password","email"), false);
            String sql = "SELECT * FROM auth;";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertTrue(rs.next());
            }
            new SQLAuthDAO().clear();
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertFalse(rs.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulGetUserTest() {
        try {
            String sql = "INSERT INTO user(username, password, email) VALUES (?,?,?)";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                statement.setString(1, "user");
                statement.setString(2, "password");
                statement.setString(3, "email");
                var rs = statement.executeUpdate();
                if(rs != 1) {
                    throw new RuntimeException("Did not add");
                }
            }
            var testResponse = new SQLUserDAO().getUser("user");
            Assertions.assertTrue(testResponse != null && testResponse.username().equals("user"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void nullGetUserTest() {
        try {
            String sql = "INSERT INTO user(username, password, email) VALUES (NULL,?,?)";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                statement.setString(1, "password");
                statement.setString(2, "email");
                statement.executeUpdate();
            } catch (SQLException e) {}
            var testResponse = new SQLUserDAO().getUser(null);
            Assertions.assertNull(testResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getUserLongUsernameTest() {
        try {
            String sql = "INSERT INTO user(username, password, email) VALUES (NULL,?,?)";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                statement.setString(1, "this is a very long string which exceeds the limit enacted by the SQL syntax");
                statement.setString(2, "password");
                statement.setString(3, "email");
                statement.executeUpdate();
            } catch (SQLException e) {}
            var testResponse = new SQLUserDAO().getUser("this is a very long string which exceeds the limit enacted by the SQL syntax");
            Assertions.assertNull(testResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulCreateUserTest() {
        try {
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            String sql = "SELECT * FROM user WHERE username = \"username\"";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertTrue(rs.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void nullCreateUserTest() {
        try {
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLUserDAO().createUser(new UserData(null, null, null)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createUserLongUsernameTest() {
        try {
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLUserDAO().createUser(new UserData("this is a very long string which exceeds the limit enacted by the SQL syntax",
                                                             "password", "email")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createDuplicateUserTest() {
        try {
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            Assertions.assertThrows(DataAccessException.class, () ->
                                    new SQLUserDAO().createUser(new UserData("username", "newpassword", "newEmail")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulCreateAuthTest() {
        try {
            String authToken = UUID.randomUUID().toString();
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            new SQLAuthDAO().createAuth(new AuthData("username", authToken));
            String sql = "SELECT * FROM auth WHERE auth_token = ?";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                statement.setString(1, authToken);
                var rs = statement.executeQuery();
                Assertions.assertTrue(rs.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void nullCreateAuthTest() {
        try {
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLAuthDAO().createAuth(new AuthData(null, "token")));
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLAuthDAO().createAuth(new AuthData("username", null)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createAuthLongFieldsTest() {
        try {
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLAuthDAO().createAuth(new AuthData("this is a very long string which exceeds the limit enacted by the SQL syntax",
                            "token")));
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLAuthDAO().createAuth(new AuthData("username",
                            "this is a very long token which is too long")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createDuplicateAuthTest() {
        try {
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            AuthData data = new AuthData("username", UUID.randomUUID().toString());
            new SQLAuthDAO().createAuth(data);
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLAuthDAO().createAuth(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createAuthWithNoUserTest() {
        try {
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLAuthDAO().createAuth(new AuthData("username",
                            "valid token")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulGetAuthTest() {
        try {
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            String sql = "INSERT INTO auth(username, auth_token) VALUES (?,?)";
            String authToken = UUID.randomUUID().toString();
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                statement.setString(1, "username");
                statement.setString(2, authToken);
                var rs = statement.executeUpdate();
                if(rs != 1) {
                    throw new RuntimeException("Did not add");
                }
            }
            var testResponse = new SQLAuthDAO().getAuth(authToken);
            Assertions.assertTrue(testResponse != null && testResponse.authToken().equals(authToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void nullGetAuthTest() {
        try {
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            String sql = "INSERT INTO auth(username, auth_token) VALUES (?,NULL)";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                statement.setString(1, "username");
                statement.executeUpdate();
            } catch (SQLException e) {}
            var testResponse = new SQLAuthDAO().getAuth(null);
            Assertions.assertNull(testResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulDeleteAuthTest() {
        try {
            new SQLUserDAO().createUser(new UserData("username", "password", "email"));
            AuthData data = new AuthData("username", UUID.randomUUID().toString());
            new SQLAuthDAO().createAuth(data);
            String sql = "SELECT * FROM auth";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertTrue(rs.next());
            }
            new SQLAuthDAO().deleteAuth(data);
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertFalse(rs.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Test
    public void deleteNonexistentAuthTest() {
        Assertions.assertThrows(DataAccessException.class, () ->
                new SQLAuthDAO().deleteAuth(new AuthData("fake", "fake")));
    }

    @Test
    public void successfulCreateGameTest() {
        try {
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            String sql = "SELECT * FROM game";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                Assertions.assertTrue(rs.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulCreateDuplicateGameTest() {
        try {
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            int count = 0;
            String sql = "SELECT * FROM game";
            try(var conn = DatabaseManager.getConnection(); var statement = conn.prepareStatement(sql)) {
                var rs = statement.executeQuery();
                while(rs.next()) {
                    count++;
                }
            }
            Assertions.assertEquals(3, count);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void nullCreateGameTest() {
        try {
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLGameDAO().createGame(new GameData(-1, null, null, null, new ChessGame())));
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLGameDAO().createGame(new GameData(-1, null, null, "super fun", null)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulGetGameTest() {
        try {
            var createResult = new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            var getResult = new SQLGameDAO().getGame(createResult);
            Assertions.assertTrue(getResult != null && getResult.gameName().equals("fun game"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void gameIdResetTest() {
        try {
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            new SQLGameDAO().clear();
            var id = new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            Assertions.assertEquals(1, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void emptyListGamesTest() {
        try {
            var gamesList = new SQLGameDAO().listGames();
            Assertions.assertTrue(gamesList != null && gamesList.isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void populatedListGamesTest() {
        try {
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            new SQLGameDAO().createGame(new GameData(-1, null, null, "fun game", new ChessGame()));
            var gamesList = new SQLGameDAO().listGames();
            Assertions.assertEquals(5, gamesList.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void successfulUpdateGameTest() {
        try {
            var initialData = new GameData(-1, null, null, "fun game", new ChessGame());
            var createResult = new SQLGameDAO().createGame(initialData);
            initialData = new GameData(createResult,
                                       initialData.whiteUsername(),
                                       initialData.blackUsername(),
                                       initialData.gameName(),
                                       initialData.game());
            new SQLUserDAO().createUser(new UserData("white", "password", "email"));
            new SQLUserDAO().createUser(new UserData("black", "password", "email"));
            new SQLGameDAO().updateGame(initialData,
                                        new GameData(initialData.gameID(), "white", null, initialData.gameName(), initialData.game()));
            new SQLGameDAO().updateGame(initialData,
                                        new GameData(initialData.gameID(), "white", "black", initialData.gameName(), initialData.game()));
            new SQLGameDAO().updateGame(initialData,
                                        new GameData(initialData.gameID(), "white", "black", "new name", initialData.game()));
            initialData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
            var expectedGame = new GameData(initialData.gameID(), "white", "black", "new name", initialData.game());
            new SQLGameDAO().updateGame(initialData, expectedGame);
            Assertions.assertEquals(expectedGame, new SQLGameDAO().getGame(initialData.gameID()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void updateNonexistentGameTest() {
        try {
            var initialData = new GameData(100, null, null, "nonexistent", new ChessGame());
            new SQLUserDAO().createUser(new UserData("white", "password", "email"));
            new SQLUserDAO().createUser(new UserData("black", "password", "email"));
            var newData = new GameData(100, "white", "black", "nonexistent", new ChessGame());
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLGameDAO().updateGame(initialData, newData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void updateWithNullGameTest() {
        try {
            var initialData = new GameData(100, null, null, "null test", new ChessGame());
            int id = new SQLGameDAO().createGame(initialData);
            new SQLUserDAO().createUser(new UserData("white", "password", "email"));
            new SQLUserDAO().createUser(new UserData("black", "password", "email"));
            var newData = new GameData(id, "white", "black", "null test", null);
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLGameDAO().updateGame(initialData, null));
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLGameDAO().updateGame(initialData, newData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void updateGameBadUsernameTest() {
        try {
            var initialData = new GameData(100, null, null, "existent", new ChessGame());
            int id = new SQLGameDAO().createGame(initialData);
            var newData = new GameData(id, "white", "black", "existent", null);
            Assertions.assertThrows(DataAccessException.class, () ->
                    new SQLGameDAO().updateGame(initialData, newData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
