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
        CurrentMatchModel match = getMatchModel(uuid);
        return matchMapper.toDto(match);
    }

    public CurrentMatchModel getMatchModel(UUID uuid) {
        CurrentMatchModel match = matches.get(uuid);
        if (match == null) {
            throw new MatchNotFoundException("Match with uuid " + uuid + " not found");
        }
        return match;
    }

    public void removeMatch(UUID uuid) {
        matches.remove(uuid);
    }
}
