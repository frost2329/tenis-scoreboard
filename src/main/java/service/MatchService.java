package service;

import entity.CurrentMatch;
import entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MatchService {
    private static final MatchService INSTANCE = new MatchService();
    private  MatchService() {}
    public static MatchService getInstance() {
        return INSTANCE;
    }
    private static final Map<UUID, CurrentMatch> matches = new ConcurrentHashMap<>();
    private  final PlayerService playerService = new PlayerService();

    public UUID createMatch(String playerName1, String playerName2) {

        Player player1 = playerService.getPlayer(playerName1);
        Player player2 = playerService.getPlayer(playerName2);

        //создать id матча
        CurrentMatch currentMatch = new CurrentMatch(player1, player2);

        matches.put(currentMatch.getId(), currentMatch);

        return currentMatch.getId();
    }

    public CurrentMatch getCurrentMatch(UUID uuid) throws Exception {
        CurrentMatch currentMatch = matches.get(uuid);
        if (currentMatch == null) {
            throw new Exception("Не найден текущий матч");
        }
        return currentMatch;
    }
}
