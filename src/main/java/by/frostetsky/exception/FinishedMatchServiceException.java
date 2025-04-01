package by.frostetsky.exception;

public class FinishedMatchServiceException extends RuntimeException {
    public FinishedMatchServiceException(String message, Exception e) {
        super(message, e);
    }
}
