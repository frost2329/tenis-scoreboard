package by.frostetsky.service;

import by.frostetsky.dto.MatchDto;
import by.frostetsky.exception.GameFinishedException;
import by.frostetsky.exception.MatchScoreCalculatorServiceException;
import by.frostetsky.mapper.MatchMapper;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.model.PlayerScore;
import by.frostetsky.model.Point;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchScoreCalculatorService {
    private static final Integer START_SCORE  = 0;
    private static final Integer GAMES_TO_WIN = 6;
    private static final Integer SETS_TO_WIN = 2;
    private static final Integer TIEBREAK_POINTS_TO_WIN = 7;
    private static final Integer DIFFERENCE_ENOUGH_TO_WIN  = 2;

    private static final MatchScoreCalculatorService INSTANCE = new MatchScoreCalculatorService();
    public static MatchScoreCalculatorService getInstance() {
        return INSTANCE;
    }

    private final OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    private final FinishedMatchService finishedMatchService = FinishedMatchService.getInstance();
    private final MatchMapper matchMapper = new MatchMapper();

    public MatchDto updateScore (UUID uuid, Integer playerId) {
        CurrentMatchModel match = ongoingMatchService.getMatchModel(uuid);
        if (match.isFinished()) {
            throw new GameFinishedException("Game is finished");
        }
        try {
            if (match.getFirstPlayerId() == playerId) {
                addPoint(match, match.getFirstPlayerScore(), match.getSecondPlayerScore());
            } else if (match.getSecondPlayerId() == playerId) {
                addPoint(match, match.getSecondPlayerScore(), match.getFirstPlayerScore());
            } else {
                throw new MatchScoreCalculatorServiceException("Не удалось получить счет игрока id " + playerId);
            }
        } catch (RuntimeException e) {
            log.error("Exception occurred", e);
            throw new MatchScoreCalculatorServiceException("Ошибка при обновлении счета матча", e);
        }
        log.info("Match score was  {}", match);

        if (match.isFinished()) {
            log.info("Game finished");
            finishedMatchService.saveMatch(match);
            ongoingMatchService.removeMatch(match.getUuid());
        }
        return matchMapper.toDto(match);
    }

    private void addPoint(CurrentMatchModel match, PlayerScore score, PlayerScore opponentScore) {
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
        if (points >= TIEBREAK_POINTS_TO_WIN && points - opponentPoints  >= DIFFERENCE_ENOUGH_TO_WIN) {
            score.setTieBreakPoints(START_SCORE);
            opponentScore.setTieBreakPoints(START_SCORE);
            addGame(match, score, opponentScore);
        }
    }

    private void addGame(CurrentMatchModel match, PlayerScore score, PlayerScore opponentScore) {
        int games = score.getGames();
        int opponentGames = opponentScore.getGames();
        score.setGames(++games);
        if (games == GAMES_TO_WIN) {
            if (games - opponentGames >= DIFFERENCE_ENOUGH_TO_WIN) {
                score.setGames(START_SCORE);
                opponentScore.setGames(START_SCORE);
                addSet(match, score);
            } else if (opponentGames == GAMES_TO_WIN) {
                match.setTieBreak(true);
            }
        } else if (games > GAMES_TO_WIN) {
            match.setTieBreak(false);
            score.setGames(START_SCORE);
            opponentScore.setGames(START_SCORE);
            addSet(match, score);
        }
    }

    private void addSet(CurrentMatchModel match, PlayerScore score) {
        score.setSets(score.getSets()+1);
        if(score.getSets() == SETS_TO_WIN) {
            match.setWinner(match.getFirstPlayerScore().getSets() == SETS_TO_WIN
                    ? match.getFirstPlayerId()
                    : match.getSecondPlayerId());
            match.setFinished(true);
        }
    }
}
