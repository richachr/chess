package server.handler;

import com.google.gson.GsonBuilder;
import request.LogoutRequest;
import server.Server;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        LogoutRequest LogoutReq = new LogoutRequest(req.headers("authorization"));
        String jsonResponse = "";
        try {
            UserService.logout(LogoutReq);
            res.status(200);
            jsonResponse = gsonBuilder.toJson(new Object());
            res.body(jsonResponse);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return jsonResponse;
    }
}