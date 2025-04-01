package by.frostetsky.exception;

public class MatchScoreCalculatorServiceException extends RuntimeException {
    public MatchScoreCalculatorServiceException(String message, Exception e) {
        super(message, e);
    }
    public MatchScoreCalculatorServiceException(String message) {
        super(message);
    }
}
