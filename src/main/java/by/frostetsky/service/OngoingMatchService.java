package by.frostetsky.service;

import by.frostetsky.dto.MatchDto;
import by.frostetsky.exception.MatchNotFoundException;
import by.frostetsky.mapper.MatchMapper;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.dto.PlayerDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OngoingMatchService {
    private static final OngoingMatchService INSTANCE = new OngoingMatchService();
    public static OngoingMatchService getInstance() {
        return INSTANCE;
    }

    private final PlayerService playerService = new PlayerService();
    private final MatchMapper matchMapper = new MatchMapper();
    private final Map<UUID, CurrentMatchModel> matches = new ConcurrentHashMap<>();

    public UUID createMatch(String firstPlayerName, String secondPlayerName) {
        PlayerDto firstPlayerDto = playerService.getOrCreatePlayer(firstPlayerName);
        PlayerDto secondPlayerDto = playerService.getOrCreatePlayer(secondPlayerName);
        CurrentMatchModel currentMatchModel = new CurrentMatchModel(firstPlayerDto, secondPlayerDto);
        matches.put(currentMatchModel.getUuid(), currentMatchModel);
        log.info("Current match was crated  {}", currentMatchModel);
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
        getMatchModel(uuid);
        log.info("Current match was removed  uuid {}", uuid);
        matches.remove(uuid);
    }
}
