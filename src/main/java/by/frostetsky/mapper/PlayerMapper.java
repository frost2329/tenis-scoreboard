package by.frostetsky.mapper;

import by.frostetsky.entity.Player;
import by.frostetsky.model.PlayerModel;

public class PlayerMapper {
    private static final PlayerMapper INSTANCE = new PlayerMapper();
    public static PlayerMapper getInstance() {return INSTANCE;}
    private  PlayerMapper() {}

    public PlayerModel toPlayer(Player player) {
        return new PlayerModel(player.getId(), player.getName());
    }
}
