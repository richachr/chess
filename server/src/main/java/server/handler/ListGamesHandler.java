package server.handler;

import com.google.gson.Gson;
import request.ListGamesRequest;
import server.Server;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler extends Handler implements Route {
    public ListGamesHandler(boolean useMemoryDao) {
        super(useMemoryDao);
    }

    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new Gson();
        ListGamesRequest listReq = new ListGamesRequest(req.headers("authorization"));
        String jsonResponse = "";
        try {
            var listRes = GameService.listGames(listReq, useMemoryDao);
            res.status(200);
            jsonResponse = gsonBuilder.toJson(listRes, listRes.getClass());
            res.body(jsonResponse);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return jsonResponse;
    }
}
