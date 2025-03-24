package model;

import lombok.Data;

import java.util.UUID;


@Data
public class CurrentMatch {
    private UUID uuid;

    private int firstPlayerId;
    private String firstPlayerName;
    private CurrentMatchPlayerScore firstPlayerScore;

    private int secondPlayerId;
    private String secondPlayerName;
    private CurrentMatchPlayerScore secondPlayerScore;

    private boolean finished = false;

    public CurrentMatch(Player firstPlayer, Player secondPlayer) {
        this.uuid = UUID.randomUUID();
        this.firstPlayerId = firstPlayer.getId();
        this.firstPlayerName = firstPlayer.getName();
        this.firstPlayerScore = new CurrentMatchPlayerScore();

        this.secondPlayerId = secondPlayer.getId();
        this.secondPlayerName = secondPlayer.getName();
        this.secondPlayerScore = new CurrentMatchPlayerScore();
    }
}
