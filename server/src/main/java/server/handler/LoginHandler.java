package server.handler;

import com.google.gson.GsonBuilder;
import request.LoginRequest;
import server.Server;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        LoginRequest LoginReq = gsonBuilder.fromJson(req.body(), LoginRequest.class);
        String jsonResponse = "";
        try {
            var LoginRes = UserService.login(LoginReq);
            res.status(200);
            jsonResponse = gsonBuilder.toJson(LoginRes, LoginRes.getClass());
            res.body(jsonResponse);
        } catch (Exception e) {
            Server.handleException(e);
        }
        System.out.println(jsonResponse);
        return jsonResponse;
    }
}
