package ui;

import facade.ServerFacade;
import websocket.ClientData;

public interface Client {
    void printHelp();

    ClientData processInput(String input, ServerFacade facade);

    default void printError(String error) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "[ERROR]: " + error + EscapeSequences.RESET_TEXT_COLOR);
    }
}
