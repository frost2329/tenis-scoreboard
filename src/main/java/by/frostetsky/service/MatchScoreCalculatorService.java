package by.frostetsky.service;

import by.frostetsky.exception.GameFinishedException;
import by.frostetsky.exception.MatchScoreCalculatorServiceException;
import by.frostetsky.model.CurrentMatchModel;

public class MatchScoreCalculatorService {
    private static final MatchScoreCalculatorService INSTANCE = new MatchScoreCalculatorService();
    private  MatchScoreCalculatorService() {}
    public static MatchScoreCalculatorService getInstance() {return INSTANCE;}


    public void addPointToPlayer(CurrentMatchModel match, Integer playerId) throws GameFinishedException {
        if (match.getFirstPlayerId() == playerId) {
            match.addPoint(match.getFirstPlayerScore(), match.getSecondPlayerScore());
        } else if (match.getSecondPlayerId() == playerId) {
            match.addPoint(match.getSecondPlayerScore(), match.getFirstPlayerScore());
        } else {
            throw new MatchScoreCalculatorServiceException("Не удалось получить счет игрока id " + playerId);
        }
    }

}
