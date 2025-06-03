import server.ServerFacade;
import ui.InputLoop;

public class Main {
    public static void main(String[] args) {
        String url = "http://localhost:8080";
        if(args.length > 0) {
            url = args[0];
        }
        var facade = new ServerFacade(url);

        System.out.println("\n♕ 240 Chess Client: type \"help\" for a list of commands. ♕\n");
        new InputLoop(facade).run();
    }
}