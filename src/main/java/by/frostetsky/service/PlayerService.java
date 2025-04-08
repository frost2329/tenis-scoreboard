package by.frostetsky.service;

import by.frostetsky.dto.PlayerDto;
import by.frostetsky.exception.PlayerNotFoundException;
import by.frostetsky.exception.PlayerServiceException;
import by.frostetsky.mapper.PlayerMapper;
import by.frostetsky.entity.Player;
import by.frostetsky.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerService {
    private final PlayerMapper playerMapper = new PlayerMapper();
    private final PlayerRepository playerRepository = new PlayerRepository();

    public PlayerDto getOrCreatePlayer(String name) {
        try {
            Player player = createPlayer(name);
            return playerMapper.toDto(player);
        } catch (Exception e) {
            Player player = getPlayer(name);
            return playerMapper.toDto(player);
        }
    }

    public Player createPlayer(String name) {
        try {
            Player player = Player.builder().name(name).build();
            player = playerRepository.save(player);
            log.info("Player was crated {}", player);
            return player;
        } catch (Exception e) {
            throw new PlayerServiceException("Error saving player in service", e);
        }
    }

    public Player getPlayer(String name) {
        try {
            Player player = playerRepository.findByName(name)
                    .orElseThrow(() -> new PlayerNotFoundException(name));
            log.info("Player successfully received {}", player);
            return player;
        } catch (Exception e) {
            throw new PlayerServiceException("Error receiving player in service", e);
        }
    }
}
