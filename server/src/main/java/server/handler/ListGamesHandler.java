package server.handler;

import com.google.gson.GsonBuilder;
import request.ListGamesRequest;
import server.Server;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route {
    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        ListGamesRequest listReq = new ListGamesRequest(req.headers("authorization"));
        String jsonResponse = "";
        try {
            var listRes = GameService.listGames(listReq);
            res.status(200);
            jsonResponse = gsonBuilder.toJson(listRes, listRes.getClass());
            res.body(jsonResponse);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return jsonResponse;
    }
}
