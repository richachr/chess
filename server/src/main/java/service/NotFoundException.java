package service;

public class NotFoundException extends Exception {

  public NotFoundException() {
    super("not found");
  }

  public NotFoundException(String message) {
        super(message);
    }
}
