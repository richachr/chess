package ui;

import server.ServerFacade;

public interface Client {
    public void printHelp();
    public ClientSwitchRequest processInput(String input, ServerFacade facade);
    public default void printError(String error) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "[ERROR]: " + error + EscapeSequences.RESET_TEXT_COLOR);
    }
}
