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

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user","application/json", (req, res) -> new RegisterHandler().handle(req, res));
        Spark.post("/session", "application/json", (req, res) -> new LoginHandler().handle(req, res));
        Spark.delete("/session", "application/json", (req, res) -> new LogoutHandler().handle(req, res));
        Spark.get("/game", "application/json", (req, res) -> new ListGamesHandler().handle(req, res));
        Spark.post("/game", "application/json", (req, res) -> new CreateGameHandler().handle(req, res));
        Spark.put("/game", "application/json", (req, res) -> new JoinGameHandler().handle(req, res));
        Spark.delete("/db", "application/json", (req, res) -> new ClearHandler().handle(req,res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void  handleException(Exception e) {
        var gsonBuilder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
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
