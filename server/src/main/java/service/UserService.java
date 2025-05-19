package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.UUID;

public class UserService {
    public RegisterResult register(RegisterRequest req) throws BadRequestException, AlreadyTakenException {
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
        auth.createAuth(new AuthData(authToken,req.username()));
        return new RegisterResult(req.username(),authToken);
    }
}
