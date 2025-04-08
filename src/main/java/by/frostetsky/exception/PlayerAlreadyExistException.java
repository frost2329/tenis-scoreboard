package by.frostetsky.exception;

public class PlayerAlreadyExistException extends RuntimeException {
    public PlayerAlreadyExistException(Exception e) {
        super("Player with that name already exists", e);
    }
}
