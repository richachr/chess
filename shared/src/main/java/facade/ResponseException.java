package facade;

public class ResponseException extends Exception {
    public Integer statusCode;

    public ResponseException(String message) {
        super(message);
        this.statusCode = null;
    }

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

}
