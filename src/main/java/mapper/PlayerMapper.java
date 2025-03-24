package mapper;

import entity.PlayerEntity;
import model.Player;
import service.MatchService;

public class PlayerMapper {
    private static final PlayerMapper INSTANCE = new PlayerMapper();
    public static PlayerMapper getInstance() {return INSTANCE;}
    private  PlayerMapper() {}

    public Player toPlayer(PlayerEntity player) {
        return new Player(player.getId(), player.getName());
    }
}
