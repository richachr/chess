package server.handler;

import com.google.gson.GsonBuilder;
import server.Server;
import server.websocket.Connection;
import server.websocket.ConnectionManager;
import service.TestService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler extends Handler implements Route {
    public ClearHandler(boolean useMemoryDao) {
        super(useMemoryDao);
    }

    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        try {
            TestService.clear(useMemoryDao);
            ConnectionManager.connections.clear();
            res.status(200);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return gsonBuilder.toJson(new Object());
    }
}
