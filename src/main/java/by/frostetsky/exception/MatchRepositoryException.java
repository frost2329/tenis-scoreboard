package by.frostetsky.exception;

public class MatchRepositoryException extends RuntimeException {
    public MatchRepositoryException(String message, Exception e) {
        super(message, e);
    }
}
