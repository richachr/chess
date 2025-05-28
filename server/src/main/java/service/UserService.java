package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

import java.util.UUID;

public class UserService extends Service {
    public static RegisterResult register(RegisterRequest req, boolean useMemoryDao) throws BadRequestException, AlreadyTakenException, DataAccessException {
        if(req.username() == null ||
           req.email() == null ||
           req.password() == null) {
            throw new BadRequestException();
        }
        UserDAO users;
        AuthDAO auths;
        if(useMemoryDao) {
            users = new MemoryUserDAO();
            auths = new MemoryAuthDAO();
        } else {
            users = new SQLUserDAO();
            auths = new SQLAuthDAO();
        }
        UserData existingUserData = users.getUser(req.username());
        if(existingUserData != null) {
            throw new AlreadyTakenException();
        }
        users.createUser(new UserData(req.username(), BCrypt.hashpw(req.password(),BCrypt.gensalt()), req.email()));
        String authToken = UUID.randomUUID().toString();
        auths.createAuth(new AuthData(req.username(), authToken));
        return new RegisterResult(req.username(), authToken);
    }

    public static LoginResult login(LoginRequest req, boolean useMemoryDao) throws BadRequestException, NotFoundException, UnauthorizedException, DataAccessException {
        if(req.username() == null ||
           req.password() == null) {
            throw new BadRequestException();
        }
        UserDAO users;
        AuthDAO auths;
        if(useMemoryDao) {
            users = new MemoryUserDAO();
            auths = new MemoryAuthDAO();
        } else {
            users = new SQLUserDAO();
            auths = new SQLAuthDAO();
        }
        var userData = users.getUser(req.username());
        if(userData == null) {
            throw new NotFoundException("user not found");
        }
        if(!BCrypt.checkpw(req.password(),userData.password())) {
            throw new UnauthorizedException();
        }
        String authToken = UUID.randomUUID().toString();
        auths.createAuth(new AuthData(req.username(), authToken));
        return new LoginResult(req.username(), authToken);
    }

    public static void logout(LogoutRequest req, boolean useMemoryDao) throws BadRequestException, NotFoundException, InternalErrorException, DataAccessException {
        if(req.authToken() == null) {
            throw new BadRequestException();
        }
        AuthDAO auths;
        if(useMemoryDao) {
            auths = new MemoryAuthDAO();
        } else {
            auths = new SQLAuthDAO();
        }
        var authData = auths.getAuth(req.authToken());
        if(authData == null) {
            throw new NotFoundException();
        }
        auths.deleteAuth(authData);
        if(auths.getAuth(req.authToken()) != null) {
            throw new InternalErrorException("deletion failed");
        }
    }
}
