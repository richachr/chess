package ui;

import facade.ServerFacade;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.GameState.*;

public class InputLoop implements NotificationHandler {
    GameState state = LOGGED_OUT;
    String prefix = state.toString();
    ServerFacade facade;
    Client currentClient = new LoggedOutClient();

    public InputLoop(ServerFacade facade) {
        this.facade = facade;
    }


    public void run() {
        String userInput = "";
        Scanner inputScanner = new Scanner(System.in);
        inputScanner.useDelimiter("\n");
        ClientSwitchRequest switchRequest = null;
        while (!userInput.equalsIgnoreCase("quit")) {
            System.out.printf("%s > " + EscapeSequences.SET_TEXT_BLINKING, prefix);
            userInput = inputScanner.nextLine();
            System.out.print(EscapeSequences.RESET_TEXT_BLINKING);
            try {
                switchRequest = currentClient.processInput(userInput, facade);
            } catch (Exception e) {
                System.err.println("An error has occurred.");
            }
            if(switchRequest != null) {
                switchState(switchRequest);
            }
        }
    }

    private void switchState(ClientSwitchRequest switchRequest) {
        switch(state) {
            case LOGGED_IN -> {
                if(switchRequest.authToken() == null) {
                    state = LOGGED_OUT;
                    prefix = state.toString();
                    currentClient = new LoggedOutClient();
                } else {
                    assert (switchRequest.gameId() != null);
                    state = IN_GAME;
                    currentClient = new InGameClient(switchRequest.authToken(),
                            switchRequest.username(),
                            switchRequest.gameId(),
                            switchRequest.playerColor());
                }
            }
            case LOGGED_OUT -> {
                assert (switchRequest.authToken() != null);
                state = LOGGED_IN;
                prefix = switchRequest.username();
                currentClient = new LoggedInClient(switchRequest.authToken(), switchRequest.username());
            }
            case IN_GAME -> {
                assert(switchRequest.gameId() == null);
                state = LOGGED_IN;
                currentClient = new LoggedInClient(switchRequest.authToken(), switchRequest.username());
            }
        }
    }

    public void notify(ServerMessage notification) {

    }
}
