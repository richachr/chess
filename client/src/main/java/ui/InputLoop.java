package ui;

import facade.ServerFacade;
import websocket.ClientData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
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
        ClientData switchRequest = null;
        while (!userInput.equalsIgnoreCase("quit")) {
            printUserPrompt();
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

    private void switchState(ClientData switchRequest) {
        switch(state) {
            case LOGGED_IN -> {
                if(switchRequest.authToken() == null) {
                    state = LOGGED_OUT;
                    prefix = state.toString();
                    currentClient = new LoggedOutClient();
                } else {
                    assert (switchRequest.gameId() != null);
                    state = IN_GAME;
                    currentClient = new InGameClient(switchRequest, facade.getUrl(), this);
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
        assert(currentClient.getClass() == InGameClient.class);
        System.out.println(EscapeSequences.ERASE_LINE);
        switch(notification.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGameMessage loadGameMessage = (LoadGameMessage) notification;
                loadGame(loadGameMessage);
            }
            case ERROR -> {
                ErrorMessage errorMessage = (ErrorMessage) notification;
                printErrorMessage(errorMessage);
            }
            case NOTIFICATION -> {
                NotificationMessage notificationMessage = (NotificationMessage) notification;
                printNotification(notificationMessage);
            }
        }
        printUserPrompt();
    }

    private void loadGame(LoadGameMessage notification) {
        InGameClient wsClient = (InGameClient) currentClient;
        wsClient.loadGame(notification.game);
    }

    private void printErrorMessage(ErrorMessage notification) {
        currentClient.printError(notification.errorMessage.replaceAll("Error: ", ""));
    }

    private void printNotification(NotificationMessage notification) {
        System.out.println(notification.message);
    }

    private void printUserPrompt() {
        System.out.printf("%s > " + EscapeSequences.SET_TEXT_BLINKING, prefix);
    }
}
