package service;

import dao.PlayerRepository;
import entity.Player;

public class PlayerService {

    PlayerRepository playerRepository = new PlayerRepository();

    public Player getPlayer(String name) {
        Player player = playerRepository.getByName(name);
        if(player == null) {
            player = createPlayer(name);
        }
        return player;
    }

    public Player createPlayer(String name) {
        return playerRepository.create(name);
    }
}
