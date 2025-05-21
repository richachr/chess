package server.handler;

import com.google.gson.GsonBuilder;
import request.CreateGameRequest;
import server.Server;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        CreateGameRequest temp = gsonBuilder.fromJson(req.body(), CreateGameRequest.class);
        CreateGameRequest createReq = new CreateGameRequest(temp.gameName(), req.headers("authorization"));
        String jsonResponse = "";
        try {
            var createRes = GameService.createGame(createReq);
            res.status(200);
            jsonResponse = gsonBuilder.toJson(createRes, createRes.getClass());
            res.body(jsonResponse);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return jsonResponse;
    }
}
