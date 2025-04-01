package by.frostetsky;

import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.entity.Player;
import by.frostetsky.repository.MatchRepository;
import by.frostetsky.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchRepositoryTest {


    @Test
    public void checkSaveAndReadMatch() {
        var sessionFactory = HibernateUtil.getSessionFactory();
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Player player1 = Player.builder().name("Den").build();
        Player player2 = Player.builder().name("Pen").build();
        session.save(player2);
        session.save(player1);

        for (int i = 0; i < 10; i++) {
            FinishedMatch match = FinishedMatch.builder()
                    .firstPlayer(player1)
                    .secondPlayer(player2)
                    .winner(player1)
                    .build();

            session.save(match);
        }

        MatchRepository matchRepository = new MatchRepository();

        List<FinishedMatch> matches = matchRepository.findAll(2, 3, null);
        for(FinishedMatch match : matches) {
            System.out.println(match);
        }

        session.getTransaction().commit();

        assertEquals(3, matches.size());
        assertTrue(matches.stream().allMatch(m -> "Den".equals(m.getFirstPlayer().getName())));
    }

    @Test
    public void checkGetTotalCount() {
        final int matchCount = 15;
        var sessionFactory = HibernateUtil.getSessionFactory();
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Player player1 = Player.builder().name("Den").build();
        Player player2 = Player.builder().name("Pen").build();
        session.save(player2);
        session.save(player1);

        MatchRepository matchRepository = new MatchRepository();

        for (int i = 0; i < matchCount; i++) {
            FinishedMatch match = FinishedMatch.builder()
                    .firstPlayer(player1)
                    .secondPlayer(player2)
                    .winner(player1)
                    .build();

            matchRepository.save(match);
        }
        assertEquals(matchCount, matchRepository.getTotalCount(), "Количество матчей должно быть " + matchCount);

    }
}
