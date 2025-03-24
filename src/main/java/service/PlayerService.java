package service;

import dao.PlayerRepository;
import entity.PlayerEntity;

public class PlayerService {
    private static final PlayerService INSTANCE = new PlayerService();
    private  PlayerService() {}
    public static PlayerService getInstance() {return INSTANCE;}

    PlayerRepository playerRepository = new PlayerRepository();

    public PlayerEntity getPlayer(String name) {
        PlayerEntity playerEntity = playerRepository.getByName(name);
        if(playerEntity == null) {
            playerEntity = createPlayer(name);
        }
        return playerEntity;
    }

    public PlayerEntity createPlayer(String name) {
        return playerRepository.create(name);
    }
}
