package ui;

import java.util.Scanner;

import static ui.GameState.*;

public class InputLoop {
    GameState state = LOGGED_OUT;
    String prefix = state.toString();

    public void run() {
        String userInput;
        Scanner inputScanner = new Scanner(System.in);
        do {
            System.out.printf(" %s > " + EscapeSequences.SET_TEXT_BLINKING, prefix);
            userInput = inputScanner.nextLine();
            System.out.print(EscapeSequences.RESET_TEXT_BLINKING);
        } while (!userInput.equalsIgnoreCase("quit"));
    }

}
