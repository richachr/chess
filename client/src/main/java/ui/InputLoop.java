package ui;

import server.ServerFacade;

import java.util.Scanner;

import static ui.GameState.*;

public class InputLoop {
    GameState state = LOGGED_OUT;
    String prefix = state.toString();
    ServerFacade facade;

    public InputLoop(ServerFacade facade) {
        this.facade = facade;
    }


    public void run() {
        String userInput = "";
        Scanner inputScanner = new Scanner(System.in);
        inputScanner.useDelimiter("\n");
        while (!userInput.equalsIgnoreCase("quit")) {
            System.out.printf("%s > " + EscapeSequences.SET_TEXT_BLINKING, prefix);
            userInput = inputScanner.nextLine();
            System.out.print(EscapeSequences.RESET_TEXT_BLINKING);
            state.getClient().processInput(userInput);
        }
    }

}
