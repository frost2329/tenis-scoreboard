package by.frostetsky.model;

import by.frostetsky.dto.PlayerDto;
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

    public void addPoint(PlayerScore score, PlayerScore opponentScore) throws GameFinishedException {
        if (this.isFinished()) {
            throw new GameFinishedException("Игра завершена, добавить очко невозможно");
        }
        int points = score.getPoints();
        int opponentPoints = opponentScore.getPoints();

        if (this.isTieBreak) {
            addPointForTieBreak(score, opponentScore);
            return;
        }
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

    private void addPointForTieBreak(PlayerScore score, PlayerScore opponentScore) {
        int points = score.getPoints();
        int opponentPoints = opponentScore.getPoints();
        if (points < 6) {
            score.setPoints(points + 1);
        } else {
            if (points - opponentPoints >= 1) {
                score.setPoints(0);
                opponentScore.setPoints(0);
                addGame(score, opponentScore);
            } else {
                score.setPoints(points+1);
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
                this.isTieBreak = true;
                score.setGames(games+1);
            }
        }
        else if (games == 6) {
            this.isTieBreak = false;
            score.setGames(0);
            opponentScore.setGames(0);
            addSet(score);
        }
    }

    private void addSet(PlayerScore score) {
        score.setSets(score.getSets()+1);
        if(score.getSets() == 2) {
            this.winner = this.getFirstPlayerScore().getSets() == 2 ? this.firstPlayerId : this.secondPlayerId;
            this.isFinished = true;
        }
    }
}
