package by.frostetsky.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String name) {
        super("Player not found with name " + name);
    }
}
