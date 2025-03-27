package by.frostetsky.service;

import by.frostetsky.exception.GameFinishedException;
import by.frostetsky.exception.MatchNotFoundException;
import by.frostetsky.mapper.PlayerMapper;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.model.PlayerModel;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchService {
    private static final OngoingMatchService INSTANCE = new OngoingMatchService();
    public static OngoingMatchService getInstance() {
        return INSTANCE;
    }
    private OngoingMatchService() {}

    private final PlayerService playerService = PlayerService.getInstance();
    private final PlayerMapper playerMapper = PlayerMapper.getInstance();
    private final MatchScoreCalculatorService matchScoreCalculatorService = MatchScoreCalculatorService.getInstance();
    private final FinishedMatchService finishedMatchService = FinishedMatchService.getInstance();

    private final Map<UUID, CurrentMatchModel> matches = new ConcurrentHashMap<>();

    public UUID createMatch(String firstPlayerName, String secondPlayerName) {
        PlayerModel firstPlayer= playerMapper.toPlayer(playerService.getPlayer(firstPlayerName));
        PlayerModel secondPlayer = playerMapper.toPlayer(playerService.getPlayer(secondPlayerName));
        CurrentMatchModel currentMatchModel = new CurrentMatchModel(firstPlayer, secondPlayer);
        matches.put(currentMatchModel.getUuid(), currentMatchModel);
        return currentMatchModel.getUuid();
    }

    public CurrentMatchModel getCurrentMatch(UUID uuid) {
        CurrentMatchModel currentMatchModel = matches.get(uuid);
        if (currentMatchModel == null) {
            throw new MatchNotFoundException("Текущий матч не найден");
        }
        return currentMatchModel;
    }

    public CurrentMatchModel addPointToPlayer(UUID uuid, Integer playerId) throws MatchNotFoundException, GameFinishedException {
        CurrentMatchModel match = getCurrentMatch(uuid);
        matchScoreCalculatorService.addPointToPlayer(match, playerId);
        if(match.isFinished()) {
            finishedMatchService.saveMatch(match);
            matches.remove(match.getUuid());
        }
        return match;
    }
}
