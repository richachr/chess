package dataaccess;

import model.AuthData;
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
                                    new SQLUserDAO().createUser(new UserData("username", "newpassword", "newemail")));
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
}
