package ui;

public interface Client {
    public void printHelp();
    public void processInput(String input);
    public default void printError(String error) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "[ERROR]: " + error + EscapeSequences.RESET_TEXT_COLOR);
    }
}
