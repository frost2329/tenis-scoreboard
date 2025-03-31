package by.frostetsky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;


public record MatchScoreRequest (
    UUID uuid,
    Integer playerId
) {};
