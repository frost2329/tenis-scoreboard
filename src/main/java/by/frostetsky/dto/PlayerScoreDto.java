package by.frostetsky.dto;

public record PlayerScoreDto(
        Integer playerId,
        String playerName,
        Integer sets,
        Integer games,
        Integer points
) {
}
