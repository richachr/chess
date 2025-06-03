package ui;

public class LoggedOutClient implements Client {

    @Override
    public void printHelp() {
        System.out.println(EscapeSequences.EMPTY + "help -> " + EscapeSequences.SET_TEXT_ITALIC + "display help text.");
        System.out.println(EscapeSequences.EMPTY + "quit -> " + EscapeSequences.SET_TEXT_ITALIC + "exit the program.");
        System.out.println(EscapeSequences.EMPTY + "login [username] [password] -> " + EscapeSequences.SET_TEXT_ITALIC + "display help text.");
        System.out.println(EscapeSequences.EMPTY + "register [username] [password] [email] -> " + EscapeSequences.SET_TEXT_ITALIC + "display help text.");
    }
}
