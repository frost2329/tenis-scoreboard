package by.frostetsky.service;

import by.frostetsky.dto.MatchDto;
import by.frostetsky.exception.MatchNotFoundException;
import by.frostetsky.mapper.MatchMapper;
import by.frostetsky.mapper.PlayerMapper;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.dto.PlayerDto;

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
    private final MatchScoreCalculatorService matchScoreCalculatorService = MatchScoreCalculatorService.getInstance();
    private final FinishedMatchService finishedMatchService = FinishedMatchService.getInstance();
    private final MatchMapper matchMapper = MatchMapper.getInstance();

    private final Map<UUID, CurrentMatchModel> matches = new ConcurrentHashMap<>();

    public UUID createMatch(String firstPlayerName, String secondPlayerName) {
        PlayerDto firstPlayerDto = playerService.getPlayer(firstPlayerName);
        PlayerDto secondPlayerDto = playerService.getPlayer(secondPlayerName);
        CurrentMatchModel currentMatchModel = new CurrentMatchModel(firstPlayerDto, secondPlayerDto);
        matches.put(currentMatchModel.getUuid(), currentMatchModel);
        return currentMatchModel.getUuid();
    }

    public MatchDto getCurrentMatch(UUID uuid) {
        CurrentMatchModel match = getMatch(uuid);
        return matchMapper.toDto(match);
    }

    public MatchDto addPointToPlayer(UUID uuid, Integer playerId) {
        CurrentMatchModel match = getMatch(uuid);
        matchScoreCalculatorService.addPointToPlayer(match, playerId);
        if(match.isFinished()) {
            finishedMatchService.saveMatch(match);
            matches.remove(match.getUuid());
        }
        return matchMapper.toDto(match);
    }

    private CurrentMatchModel getMatch(UUID uuid) {
        CurrentMatchModel match = matches.get(uuid);
        if (match == null) {
            throw new MatchNotFoundException("Match with uuid " + uuid + " not found");
        }
        return match;
    }
}
