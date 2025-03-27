package by.frostetsky.model;

import by.frostetsky.exception.GameFinishedException;
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

    private boolean finished = false;
    private boolean tieBreak = false;

    private int winner;

    public CurrentMatchModel(PlayerModel firstPlayer, PlayerModel secondPlayer) {
        this.uuid = UUID.randomUUID();
        this.firstPlayerId = firstPlayer.getId();
        this.firstPlayerName = firstPlayer.getName();
        this.firstPlayerScore = new PlayerScore();

        this.secondPlayerId = secondPlayer.getId();
        this.secondPlayerName = secondPlayer.getName();
        this.secondPlayerScore = new PlayerScore();
    }

    public void addPoint(PlayerScore score, PlayerScore opponentScore) throws GameFinishedException {
        if (this.isFinished()) {
            throw new GameFinishedException("Игра завершена, добавить очко невозможно");
        }
        int points = score.getPoints();
        int opponentPoints = opponentScore.getPoints();
        switch (points) {
            case 0 -> {
                score.setPoints(15);
            }
            case 15 -> {
                score.setPoints(30);
            }
            case 30 -> {
                score.setPoints(40);
            }
            case 40 -> {
                if(opponentPoints < 40) {
                    score.setPoints(0);
                    opponentScore.setPoints(0);
                    addGame(score, opponentScore);
                } else if (opponentPoints == 40) {
                    score.setPoints(50);
                } else if (opponentPoints == 50) {
                    opponentScore.setPoints(40);
                }
            }
            case 50 -> {
                score.setPoints(0);
                opponentScore.setPoints(0);
                addGame(score, opponentScore);
            }
        }
    }

    private void addGame(PlayerScore score, PlayerScore opponentScore) {
        int games = score.getGames();
        int opponentGames = opponentScore.getGames();
        if(games <5) {
            score.setGames(games+1);
        }
        else if (games == 5) {
            if (opponentGames < 5) {
                score.setGames(0);
                opponentScore.setGames(0);
                addSet(score);
            } else if (opponentGames == 5) {
                score.setGames(games+1);
            } else if (opponentGames == 6) {
                this.tieBreak = true;
                score.setGames(games+1);
            }
        }
        else if (games == 6) {
            this.tieBreak = false;
            score.setGames(0);
            opponentScore.setGames(0);
            addSet(score);
        }
    }

    private void addSet(PlayerScore score) {
        score.setSets(score.getSets()+1);
        if(score.getSets() == 2) {
            this.winner = this.getFirstPlayerScore().getSets() == 2 ? this.firstPlayerId : this.secondPlayerId;
            this.finished = true;
        }
    }
}
