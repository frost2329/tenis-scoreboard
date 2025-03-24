package service;

import mapper.PlayerMapper;
import model.CurrentMatch;
import model.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MatchService {
    private static final MatchService INSTANCE = new MatchService();
    public static MatchService getInstance() {return INSTANCE;}
    private  MatchService() {}

    private final PlayerService playerService = PlayerService.getInstance();
    private final PlayerMapper playerMapper = PlayerMapper.getInstance();
    private final Map<UUID, CurrentMatch> matches = new ConcurrentHashMap<>();

    public CurrentMatch getCurrentMatch(UUID uuid) throws Exception {
        CurrentMatch currentMatch = matches.get(uuid);
        if (currentMatch == null) {
            throw new Exception("Не найден текущий матч");
        }
        return currentMatch;
    }

    public UUID createMatch(String firstPlayerName, String secondPlayerName) {
        Player firstPlayer= playerMapper.toPlayer(playerService.getPlayer(firstPlayerName));
        Player secondPlayer = playerMapper.toPlayer(playerService.getPlayer(secondPlayerName));
        CurrentMatch currentMatch = new CurrentMatch(firstPlayer, secondPlayer);
        matches.put(currentMatch.getUuid(), currentMatch);
        return currentMatch.getUuid();
    }
}
