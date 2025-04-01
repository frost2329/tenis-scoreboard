package by.frostetsky.dto;

import java.util.UUID;

public record MatchDto(
        UUID uuid,
        PlayerScoreDto firstPlayer,
        PlayerScoreDto secondPlayer,
        boolean isTieBreak,
        boolean isFinished) {
}
