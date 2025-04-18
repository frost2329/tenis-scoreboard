package by.frostetsky.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message, Exception e) {
        super(message, e);
    }
    public BadRequestException(String message) {
        super(message);
    }
}
