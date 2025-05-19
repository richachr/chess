package service;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
      super("unauthorized");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
