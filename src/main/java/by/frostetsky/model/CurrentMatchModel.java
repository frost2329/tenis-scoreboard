package by.frostetsky.model;

import by.frostetsky.dto.PlayerDto;
import lombok.Data;
import java.util.UUID;


@Data
public class CurrentMatchModel {
    private UUID uuid;

    private int firstPlayerId;
    private String firstPlayerName;
    private PlayerScore firstPlayerScore;

    private int secondPlayerId;
    private String secondPlayerName;
    private PlayerScore secondPlayerScore;

    private boolean isFinished = false;
    private boolean isTieBreak = false;

    private int winner;

    public CurrentMatchModel(PlayerDto firstPlayer, PlayerDto secondPlayer) {
        this.uuid = UUID.randomUUID();
        this.firstPlayerId = firstPlayer.id();
        this.firstPlayerName = firstPlayer.name();
        this.firstPlayerScore = new PlayerScore();

        this.secondPlayerId = secondPlayer.id();
        this.secondPlayerName = secondPlayer.name();
        this.secondPlayerScore = new PlayerScore();
    }
}
