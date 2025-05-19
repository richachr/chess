package service;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("bad request");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
