package by.frostetsky.service;

import by.frostetsky.dto.PlayerDto;
import by.frostetsky.exception.PlayerServiceException;
import by.frostetsky.mapper.PlayerMapper;
import by.frostetsky.entity.Player;
import by.frostetsky.repository.PlayerRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerService {
    private static final PlayerService INSTANCE = new PlayerService();
    public static PlayerService getInstance() {
        return INSTANCE;
    }
    private final PlayerMapper playerMapper = new PlayerMapper();
    private final PlayerRepository playerRepository = new PlayerRepository();

    public PlayerDto getOrCreatePlayer(String name) {
        try {
            Optional<Player> maybePlayer = playerRepository.findByName(name);
            Player player = maybePlayer.orElseGet(()-> createPlayer(name));
            return playerMapper.toDto(player);
        } catch (Exception e) {
            throw new PlayerServiceException("Error receiving player in service", e);
        }
    }

    public Player createPlayer(String name) {
        try {
            Player player = Player.builder().name(name).build();
            log.info("Player was crated {}", player);
            playerRepository.save(player);
            return player;
        } catch (Exception e) {
            log.error("Exception occurred", e);
            throw new PlayerServiceException("Error creating player in service", e);
        }

    }
}
