package by.frostetsky.dto;

import java.util.UUID;


public record MatchScoreRequest (
        UUID uuid,
        Integer playerId) {
}
