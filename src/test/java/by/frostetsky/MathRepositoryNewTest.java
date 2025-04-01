package by.frostetsky;

import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.entity.Player;
import by.frostetsky.repository.MatchRepository;
import by.frostetsky.repository.PlayerRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathRepositoryNewTest {
    private final MatchRepository matchRepository = new MatchRepository();
    private final PlayerRepository playerRepository = new PlayerRepository();

    @Test
    public void checkSaveAndReadMatch() {
        Player player1 = Player.builder().name("Den").build();
        Player player2 = Player.builder().name("Pen").build();

        playerRepository.save(player1);
        playerRepository.save(player2);

        FinishedMatch match = FinishedMatch.builder()
                .firstPlayer(player1)
                .secondPlayer(player2)
                .winner(player1)
                .build();

        match = matchRepository.save(match);

        List<FinishedMatch> matches  = matchRepository.findAll(1, 5, "");

        for(FinishedMatch m : matches) {
            System.out.println(m.getFirstPlayer().getName());
        }


        assertEquals(match.getFirstPlayer().getName(), "Den");
        assertEquals(match.getSecondPlayer().getName(), "Pen");
    }
}
