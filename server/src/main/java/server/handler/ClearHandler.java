package server.handler;

import com.google.gson.GsonBuilder;
import server.Server;
import service.TestService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    public Object handle(Request req, Response res) {
        res.type("application/json");
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        try {
            TestService.clear();
            res.status(200);
        } catch (Exception e) {
            Server.handleException(e);
        }
        return gsonBuilder.toJson(new Object());
    }
}
