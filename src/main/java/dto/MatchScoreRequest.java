package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MatchScoreRequest {
    private UUID uuid;
    private Integer playerId;
}
