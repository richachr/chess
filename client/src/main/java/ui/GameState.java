package ui;

public enum GameState {
    LOGGED_IN,
    LOGGED_OUT,
    IN_GAME;

    public Client getClient() {
        Client clientObject = null;
        switch(this) {
            case IN_GAME -> clientObject = new InGameClient();
            case LOGGED_IN -> clientObject = new LoggedInClient();
            case LOGGED_OUT -> clientObject = new LoggedOutClient();
        }
        return clientObject;
    }
}
