package by.frostetsky;

import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.entity.Player;
import by.frostetsky.repository.MatchRepository;
import by.frostetsky.repository.PlayerRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchRepositoryTest {


    @Test
    public void checkSaveAndReadMatch() {
        Player player1 = Player.builder().name("Den").build();
        Player player2 = Player.builder().name("Pen").build();
        PlayerRepository playerRepository = new PlayerRepository();
        playerRepository.save(player1);
        playerRepository.save(player2);

        MatchRepository matchRepository = new MatchRepository();
        for (int i = 0; i < 10; i++) {
            FinishedMatch match = FinishedMatch.builder()
                    .firstPlayer(player1)
                    .secondPlayer(player2)
                    .winner(player1)
                    .build();

            matchRepository.save(match);
        }
        List<FinishedMatch> matches = matchRepository.findAll(2, 3, null);
        for(FinishedMatch match : matches) {
            System.out.println(match);
        }

        assertEquals(3, matches.size());
    }

    @Test
    public void checkGetTotalCount() {
        final int matchCount = 15;

        Player player1 = Player.builder().name("Ben").build();
        Player player2 = Player.builder().name("Len").build();

        PlayerRepository playerRepository = new PlayerRepository();
        playerRepository.save(player1);
        playerRepository.save(player2);

        MatchRepository matchRepository = new MatchRepository();
        for (int i = 0; i < matchCount; i++) {
            FinishedMatch match = FinishedMatch.builder()
                    .firstPlayer(player1)
                    .secondPlayer(player2)
                    .winner(player1)
                    .build();

            matchRepository.save(match);
        }
        assertEquals(25, matchRepository.getTotalCount(""), "Количество матчей должно быть " + matchCount);

    }
}
