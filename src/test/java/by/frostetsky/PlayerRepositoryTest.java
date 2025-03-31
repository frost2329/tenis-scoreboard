package by.frostetsky;

import by.frostetsky.entity.Player;
import by.frostetsky.repository.PlayerRepository;
import by.frostetsky.util.HibernateUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerRepositoryTest {
    @Test
    public void checkSaveSameName() {
        var sessionFactory = HibernateUtil.getSessionFactory();
        var session = sessionFactory.getCurrentSession();
        var transaction = session.beginTransaction();

        Player player1 = Player.builder().name("Victor").build();
        Player player2 = Player.builder().name("Victor").build();
        PlayerRepository playerRepository = new PlayerRepository(session);
        playerRepository.save(player1);


        assertThrows(ConstraintViolationException.class, () -> playerRepository.save(player2));
        transaction.commit();
    }
}
