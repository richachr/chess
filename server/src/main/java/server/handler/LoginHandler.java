package server.handler;

import com.google.gson.GsonBuilder;
import request.LoginRequest;
import server.Server;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler extends Handler implements Route {
    public LoginHandler(boolean useMemoryDao) {
        super(useMemoryDao);
    }

    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        LoginRequest loginReq = gsonBuilder.fromJson(req.body(), LoginRequest.class);
        String jsonResponse = "";
        try {
            var loginRes = UserService.login(loginReq, useMemoryDao);
            res.status(200);
            jsonResponse = gsonBuilder.toJson(loginRes, loginRes.getClass());
            res.body(jsonResponse);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return jsonResponse;
    }
}
