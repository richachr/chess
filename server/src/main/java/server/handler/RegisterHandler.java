package server.handler;

import com.google.gson.GsonBuilder;
import request.RegisterRequest;
import server.Server;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        RegisterRequest registerReq = gsonBuilder.fromJson(req.body(), RegisterRequest.class);
        String jsonResponse = "";
        try {
            var registerRes = UserService.register(registerReq);
            res.status(200);
            jsonResponse = gsonBuilder.toJson(registerRes, registerRes.getClass());
            res.body(jsonResponse);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return jsonResponse;
    }
}
