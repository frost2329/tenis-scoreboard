package service;

import entity.CurrentMatch;
import entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MatchService {
    PlayerService playerService = new PlayerService();
    HashMap<UUID, CurrentMatch> matches = new HashMap<>();

    public UUID createMatch(String playerName1, String playerName2) {

        Player player1 = playerService.getPlayer(playerName1);
        Player player2 = playerService.getPlayer(playerName2);

        //создать id матча
        CurrentMatch currentMatch = new CurrentMatch(player1, player2);

        matches.put(currentMatch.getId(), currentMatch);

        return currentMatch.getId();
    }
}
