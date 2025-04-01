package by.frostetsky;

import by.frostetsky.entity.Player;
import by.frostetsky.exception.PlayerRepositoryException;
import by.frostetsky.repository.PlayerRepository;
import by.frostetsky.util.HibernateUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerRepositoryTest {
    @Test
    public void checkSaveSameName() {
        PlayerRepository playerRepository = new PlayerRepository();
        Player player1 = Player.builder().name("Victor").build();
        Player player2 = Player.builder().name("Victor").build();
        playerRepository.save(player1);
        assertThrows(PlayerRepositoryException.class, () -> playerRepository.save(player2));
    }
}
