package by.frostetsky.exception;

public class PlayerRepositoryException extends RuntimeException {
    public PlayerRepositoryException(String message, Exception e) {
        super(message, e);
    }
}
