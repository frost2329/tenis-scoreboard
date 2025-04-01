package by.frostetsky.exception;

public class PlayerServiceException extends RuntimeException {
    public PlayerServiceException(String message, Exception e) {
        super(message, e);
    }
}
