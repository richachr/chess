package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.RegisterRequest;
import result.ErrorResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/game",(req, res) -> new RegisterHandler());

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

class RegisterHandler implements Route {
    public Object handle(Request req, Response res) {
        var gsonBuilder = new GsonBuilder().create();
        RegisterRequest RegisterReq = gsonBuilder.fromJson(req.body(), RegisterRequest.class);
        try {
            var RegisterRes = UserService.register(RegisterReq);
            res.status(200);
            res.body(gsonBuilder.toJson(RegisterRes));
        } catch (BadRequestException e) {
            res.status(400);
            var errorResult = new ErrorResult(e.getLocalizedMessage());
            res.body(gsonBuilder.toJson(errorResult));
        } catch (AlreadyTakenException e) {
            res.status(403);
            var errorResult = new ErrorResult(e.getLocalizedMessage());
            res.body(gsonBuilder.toJson(errorResult));
        } catch (Exception e) {
            res.status(500);
            var errorResult = new ErrorResult(e.getLocalizedMessage());
            res.body(gsonBuilder.toJson(errorResult));
        }
        return res;
    }
}
