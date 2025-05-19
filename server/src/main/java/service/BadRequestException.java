package service;

public class BadRequestException extends Exception {

    public BadRequestException() {
        super("bad request");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
