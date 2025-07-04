package server.handler;

import com.google.gson.GsonBuilder;
import request.JoinGameRequest;
import server.Server;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler extends Handler implements Route {
    public JoinGameHandler(boolean useMemoryDao) {
        super(useMemoryDao);
    }

    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        JoinGameRequest temp = gsonBuilder.fromJson(req.body(), JoinGameRequest.class);
        JoinGameRequest joinReq = new JoinGameRequest(temp.gameID(), temp.playerColor(), req.headers("authorization"));
        String jsonResponse = "";
        try {
            GameService.joinGame(joinReq, useMemoryDao);
            res.status(200);
            jsonResponse = gsonBuilder.toJson(new Object());
            res.body(jsonResponse);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return jsonResponse;
    }
}
