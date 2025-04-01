package by.frostetsky.mapper;

import by.frostetsky.entity.Player;
import by.frostetsky.dto.PlayerDto;

public class PlayerMapper {
    private static final PlayerMapper INSTANCE = new PlayerMapper();
    private  PlayerMapper() {}
    public static PlayerMapper getInstance() {
        return INSTANCE;
    }

    public PlayerDto toDto(Player player) {
        return new PlayerDto(player.getId(), player.getName());
    }
}
