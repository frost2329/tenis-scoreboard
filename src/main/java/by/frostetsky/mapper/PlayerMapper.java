package by.frostetsky.mapper;

import by.frostetsky.entity.Player;
import by.frostetsky.dto.PlayerDto;

public class PlayerMapper {
    public PlayerDto toDto(Player player) {
        return new PlayerDto(player.getId(), player.getName());
    }
}
