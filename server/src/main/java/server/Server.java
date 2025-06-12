package server;

import com.google.gson.GsonBuilder;
import result.ErrorResult;
import server.handler.*;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.NotFoundException;
import service.UnauthorizedException;
import spark.*;

public class Server {
    public static final boolean  USE_MEMORY_DAO = false;
    private final WebSocketHandler wsHandler = new WebSocketHandler(USE_MEMORY_DAO);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", wsHandler);
        Spark.post("/user","application/json", (req, res) -> new RegisterHandler(USE_MEMORY_DAO).handle(req, res));
        Spark.post("/session", "application/json", (req, res) -> new LoginHandler(USE_MEMORY_DAO).handle(req, res));
        Spark.delete("/session", "application/json", (req, res) -> new LogoutHandler(USE_MEMORY_DAO).handle(req, res));
        Spark.get("/game", "application/json", (req, res) -> new ListGamesHandler(USE_MEMORY_DAO).handle(req, res));
        Spark.post("/game", "application/json", (req, res) -> new CreateGameHandler(USE_MEMORY_DAO).handle(req, res));
        Spark.put("/game", "application/json", (req, res) -> new JoinGameHandler(USE_MEMORY_DAO).handle(req, res));
        Spark.delete("/db", "application/json", (req, res) -> new ClearHandler(USE_MEMORY_DAO).handle(req,res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void  handleException(Exception e) {
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        //noinspection IfCanBeSwitch
        if(e instanceof BadRequestException) {
            var errorResult = new ErrorResult(e.getLocalizedMessage());
            Spark.halt(400, gsonBuilder.toJson(errorResult));
        } else if(e instanceof AlreadyTakenException) {
            var errorResult = new ErrorResult(e.getLocalizedMessage());
            Spark.halt(403, gsonBuilder.toJson(errorResult));
        } else if(e instanceof UnauthorizedException) {
            var errorResult = new ErrorResult(e.getLocalizedMessage());
            Spark.halt(401, gsonBuilder.toJson(errorResult));
        } else if(e instanceof NotFoundException) {
            var errorResult = new ErrorResult(e.getLocalizedMessage());
            Spark.halt(401, gsonBuilder.toJson(errorResult));
        } else {
            var errorResult = new ErrorResult(e.getLocalizedMessage());
            Spark.halt(500, gsonBuilder.toJson(errorResult));
        }
    }
}
