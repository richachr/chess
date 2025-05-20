package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

import java.util.UUID;

public class UserService extends Service {
    public static RegisterResult register(RegisterRequest req) throws BadRequestException, AlreadyTakenException {
        if(req.username() == null ||
           req.email() == null ||
           req.password() == null) {
            throw new BadRequestException();
        }
        var users = new MemoryUserDAO();
        UserData existingUserData = users.getUser(req.username());
        if(existingUserData != null) {
            throw new AlreadyTakenException();
        }
        users.createUser(new UserData(req.username(), req.password(), req.email()));
        String authToken = UUID.randomUUID().toString();
        var auth = new MemoryAuthDAO();
        auth.createAuth(new AuthData(req.username(), authToken));
        return new RegisterResult(req.username(), authToken);
    }

    public static LoginResult login(LoginRequest req) throws BadRequestException, NotFoundException, UnauthorizedException {
        if(req.username() == null ||
           req.password() == null) {
            throw new BadRequestException();
        }
        var users = new MemoryUserDAO();
        var userData = users.getUser(req.username());
        if(userData == null) {
            throw new NotFoundException("user not found");
        }
        if(!req.password().equals(userData.password())) {
            throw new UnauthorizedException();
        }
        var auths = new MemoryAuthDAO();
        String authToken = UUID.randomUUID().toString();
        auths.createAuth(new AuthData(req.username(), authToken));
        return new LoginResult(req.username(), authToken);
    }

    public static void logout(LogoutRequest req) throws BadRequestException, NotFoundException, InternalErrorException {
        if(req.authToken() == null) {
            throw new BadRequestException();
        }
        var auths = new MemoryAuthDAO();
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
