package by.frostetsky.service;

import by.frostetsky.dto.MatchDto;
import by.frostetsky.exception.GameFinishedException;
import by.frostetsky.exception.MatchScoreCalculatorServiceException;
import by.frostetsky.mapper.MatchMapper;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.model.PlayerScore;
import by.frostetsky.model.Point;

import java.util.UUID;

public class MatchScoreCalculatorService {
    private static final MatchScoreCalculatorService INSTANCE = new MatchScoreCalculatorService();
    private  MatchScoreCalculatorService() {}
    public static MatchScoreCalculatorService getInstance() {
        return INSTANCE;
    }

    private final OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    private final FinishedMatchService finishedMatchService = FinishedMatchService.getInstance();
    private final MatchMapper matchMapper = MatchMapper.getInstance();

    public MatchDto updateScore (UUID uuid, Integer playerId) {
        CurrentMatchModel match = ongoingMatchService.getMatchModel(uuid);
        if (match.getFirstPlayerId() == playerId) {
            addPoint(match, match.getFirstPlayerScore(), match.getSecondPlayerScore());
        } else if (match.getSecondPlayerId() == playerId) {
            addPoint(match, match.getSecondPlayerScore(), match.getFirstPlayerScore());
        } else {
            throw new MatchScoreCalculatorServiceException("Не удалось получить счет игрока id " + playerId);
        }
        if(match.isFinished()) {
            finishedMatchService.saveMatch(match);
            ongoingMatchService.removeMatch(match.getUuid());
        }
        return matchMapper.toDto(match);
    }

    private void addPoint(CurrentMatchModel match, PlayerScore score, PlayerScore opponentScore) {
        if (match.isFinished()) {
            throw new GameFinishedException("Игра завершена, добавить очко невозможно");
        }
        Point points = score.getPoints();
        Point opponentPoints = opponentScore.getPoints();

        if (match.isTieBreak()) {
            addPointForTieBreak(match, score, opponentScore);
            return;
        }

        switch (points) {
            case ZERO, FIFTEEN, THIRTY -> score.setPoints(points.next());
            case FORTY -> {
                switch (opponentPoints) {
                    case FORTY -> score.setPoints(points.next());
                    case ADVANTAGE -> opponentScore.setPoints(opponentPoints.prev());
                    default -> {
                        score.setPoints(Point.ZERO);
                        opponentScore.setPoints(Point.ZERO);
                        addGame(match, score, opponentScore);
                    }
                }
            }
            case ADVANTAGE -> {
                score.setPoints(Point.ZERO);
                opponentScore.setPoints(Point.ZERO);
                addGame(match, score, opponentScore);
            }
        }
    }

    private void addPointForTieBreak(CurrentMatchModel match, PlayerScore score, PlayerScore opponentScore) {
        int points = score.getTieBreakPoints();
        int opponentPoints = opponentScore.getTieBreakPoints();
        score.setTieBreakPoints(++points);
        if (points >= 7 && points - 2 >= opponentPoints) {
            score.setTieBreakPoints(0);
            opponentScore.setTieBreakPoints(0);
            addGame(match, score, opponentScore);
        }
    }

    private void addGame(CurrentMatchModel match, PlayerScore score, PlayerScore opponentScore) {
        int games = score.getGames();
        int opponentGames = opponentScore.getGames();
        score.setGames(++games);
        if (games == 6) {
            if (opponentGames < 5) {
                score.setGames(0);
                opponentScore.setGames(0);
                addSet(match, score);
            } else if (opponentGames == 6) {
                match.setTieBreak(true);
            }
        } else if (games > 6) {
            match.setTieBreak(false);
            score.setGames(0);
            opponentScore.setGames(0);
            addSet(match, score);
        }
    }

    private void addSet(CurrentMatchModel match, PlayerScore score) {
        score.setSets(score.getSets()+1);
        if(score.getSets() == 2) {
            match.setWinner(match.getFirstPlayerScore().getSets() == 2
                    ? match.getFirstPlayerId()
                    : match.getSecondPlayerId());
            match.setFinished(true);
        }
    }
}
