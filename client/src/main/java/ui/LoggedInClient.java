package ui;

import server.ServerFacade;

public class LoggedInClient implements Client {
    @Override
    public ClientSwitchRequest processInput(String input, ServerFacade facade) {
        return null;
    }

    @Override
    public void printHelp() {
        System.out.println(EscapeSequences.EMPTY + "help -> " + EscapeSequences.SET_TEXT_ITALIC + "display help text.");
        System.out.println(EscapeSequences.EMPTY + "logout -> " + EscapeSequences.SET_TEXT_ITALIC + "log out of current session.");
        System.out.println(EscapeSequences.EMPTY + "create [gameName] -> " + EscapeSequences.SET_TEXT_ITALIC + "create a game with the specified name.");
        System.out.println(EscapeSequences.EMPTY + "list -> " + EscapeSequences.SET_TEXT_ITALIC + "list all current games.");
        System.out.println(EscapeSequences.EMPTY + "join [gameNumber] [color] -> " + EscapeSequences.SET_TEXT_ITALIC + "join a game as the specified color.");
        System.out.println(EscapeSequences.EMPTY + "observe [gameNumber] -> " + EscapeSequences.SET_TEXT_ITALIC + "join a game as an observer.");
    }
}
