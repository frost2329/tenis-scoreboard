package by.frostetsky;

import by.frostetsky.dto.MatchDto;
import by.frostetsky.exception.MatchNotFoundException;
import by.frostetsky.model.Point;
import by.frostetsky.service.MatchScoreCalculatorService;
import by.frostetsky.service.OngoingMatchService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MatchScoreCalculatorTest {
    private final OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    private final MatchScoreCalculatorService matchScoreCalculatorService = MatchScoreCalculatorService.getInstance();


    @Test
    void checkPoint() {
        var uuid = ongoingMatchService.createMatch("Artur", "Ivan");
        MatchDto matchDto = ongoingMatchService.getCurrentMatch(uuid);

        Integer firstPlayerId = matchDto.firstPlayer().playerId();
        Integer secondPlayerId = matchDto.secondPlayer().playerId();

        for (int i = 1; i <=3; i++) {
            matchDto = matchScoreCalculatorService.updateScore(uuid, firstPlayerId);
            assertEquals(matchDto.firstPlayer().points(), Point.values()[i].getValue());
        }

        for (int i = 1; i <=3; i++) {
            matchDto = matchScoreCalculatorService.updateScore(uuid,secondPlayerId);
            assertEquals(matchDto.secondPlayer().points(), Point.values()[i].getValue());
        }

        matchDto = matchScoreCalculatorService.updateScore(uuid, firstPlayerId);
        assertEquals(Point.ADVANTAGE.getValue(), matchDto.firstPlayer().points());
        assertFalse(matchDto.isFinished());

        matchDto = matchScoreCalculatorService.updateScore(uuid, secondPlayerId);
        assertFalse(matchDto.isFinished());
        assertEquals(matchDto.secondPlayer().points(), Point.FORTY.getValue());
        assertEquals(matchDto.firstPlayer().points(), Point.FORTY.getValue());

        matchDto = matchScoreCalculatorService.updateScore(uuid, firstPlayerId);
        matchDto = matchScoreCalculatorService.updateScore(uuid, firstPlayerId);

        MatchDto finalMatchDto = matchDto;
        assertAll("Error model match",
                () -> assertEquals(Point.ZERO.getValue(), finalMatchDto.secondPlayer().points()),
                () -> assertEquals(Point.ZERO.getValue(), finalMatchDto.firstPlayer().points()),
                () -> assertEquals(1, finalMatchDto.firstPlayer().games()),
                () -> assertEquals(0, finalMatchDto.secondPlayer().games()),
                () -> assertEquals(0, finalMatchDto.firstPlayer().sets()),
                () -> assertEquals(0, finalMatchDto.secondPlayer().sets())
        );

    }

    @Test
    void checkGame() {
        var uuid = ongoingMatchService.createMatch("Artur", "Ivan");
        MatchDto matchDto = ongoingMatchService.getCurrentMatch(uuid);

        Integer firstPlayerId = matchDto.firstPlayer().playerId();
        Integer secondPlayerId = matchDto.secondPlayer().playerId();

        matchDto = addPointManyTime(matchDto.uuid(), firstPlayerId, 20);
        matchDto = addPointManyTime(matchDto.uuid(), secondPlayerId, 20);
        assertEquals(5, matchDto.firstPlayer().games());
        assertEquals(5, matchDto.firstPlayer().games());

        matchDto = addPointManyTime(matchDto.uuid(), firstPlayerId, 4);
        assertEquals(6, matchDto.firstPlayer().games());
        assertEquals(0, matchDto.firstPlayer().sets());

        matchDto = addPointManyTime(matchDto.uuid(), secondPlayerId, 4);
        assertEquals(6, matchDto.secondPlayer().games());
        assertEquals(0, matchDto.secondPlayer().sets());
        assertTrue(matchDto.isTieBreak());
    }

    @Test
    void checkTieBreak() {
        var uuid = ongoingMatchService.createMatch("Artur", "Ivan");
        MatchDto matchDto = ongoingMatchService.getCurrentMatch(uuid);

        Integer firstPlayerId = matchDto.firstPlayer().playerId();
        Integer secondPlayerId = matchDto.secondPlayer().playerId();

        matchDto = addPointManyTime(matchDto.uuid(), firstPlayerId, 20);
        matchDto = addPointManyTime(matchDto.uuid(), secondPlayerId, 20);
        matchDto = addPointManyTime(matchDto.uuid(), firstPlayerId, 4);
        matchDto = addPointManyTime(matchDto.uuid(), secondPlayerId, 4);

        assertTrue(matchDto.isTieBreak());
        for (int i = 0; i < 1000; i++) {
            matchDto = matchScoreCalculatorService.updateScore(uuid, firstPlayerId);
            matchDto = matchScoreCalculatorService.updateScore(uuid, secondPlayerId);
        }
        assertEquals("1000", matchDto.secondPlayer().points());
        assertEquals("1000", matchDto.firstPlayer().points());
        assertTrue(matchDto.isTieBreak());

        matchDto = matchScoreCalculatorService.updateScore(uuid, firstPlayerId);
        matchDto = matchScoreCalculatorService.updateScore(uuid, firstPlayerId);
        assertFalse(matchDto.isTieBreak());
        MatchDto finalMatchDto = matchDto;
        assertAll("Error model match",
                () -> assertEquals(Point.ZERO.getValue(), finalMatchDto.secondPlayer().points()),
                () -> assertEquals(Point.ZERO.getValue(), finalMatchDto.firstPlayer().points()),
                () -> assertEquals(0, finalMatchDto.firstPlayer().games()),
                () -> assertEquals(0, finalMatchDto.secondPlayer().games()),
                () -> assertEquals(0, finalMatchDto.secondPlayer().sets()),
                () -> assertEquals(1, finalMatchDto.firstPlayer().sets())
        );
    }

    @Test
    void checkSet() {
        var uuid = ongoingMatchService.createMatch("Artur", "Ivan");
        MatchDto matchDto = ongoingMatchService.getCurrentMatch(uuid);

        Integer firstPlayerId = matchDto.firstPlayer().playerId();
        Integer secondPlayerId = matchDto.secondPlayer().playerId();

        matchDto = addPointManyTime(matchDto.uuid(), firstPlayerId, 24);
        matchDto = addPointManyTime(matchDto.uuid(), secondPlayerId, 24);
        MatchDto finalMatchDto = matchDto;
        assertAll("Ok",
                () -> assertEquals(Point.ZERO.getValue(), finalMatchDto.secondPlayer().points()),
                () -> assertEquals(Point.ZERO.getValue(), finalMatchDto.firstPlayer().points()),
                () -> assertEquals(0, finalMatchDto.firstPlayer().games()),
                () -> assertEquals(0, finalMatchDto.secondPlayer().games()),
                () -> assertEquals(1, finalMatchDto.secondPlayer().sets()),
                () -> assertEquals(1, finalMatchDto.firstPlayer().sets())
        );
        matchDto = addPointManyTime(matchDto.uuid(), secondPlayerId, 24);
        MatchDto finalMatchDto1 = matchDto;
        assertAll("Error model match",
                () -> assertEquals(Point.ZERO.getValue(), finalMatchDto1.secondPlayer().points()),
                () -> assertEquals(Point.ZERO.getValue(), finalMatchDto1.firstPlayer().points()),
                () -> assertEquals(0, finalMatchDto1.firstPlayer().games()),
                () -> assertEquals(0, finalMatchDto1.secondPlayer().games()),
                () -> assertEquals(2, finalMatchDto1.secondPlayer().sets()),
                () -> assertEquals(1, finalMatchDto1.firstPlayer().sets()),
                () -> assertFalse(finalMatchDto1.isTieBreak()),
                () -> assertTrue(finalMatchDto1.isFinished())
        );
    }

    @Test
    void checkError() {
        var uuid = ongoingMatchService.createMatch("Artur", "Ivan");
        MatchDto matchDto = ongoingMatchService.getCurrentMatch(uuid);
        Integer firstPlayerId = matchDto.firstPlayer().playerId();
        matchDto = addPointManyTime(matchDto.uuid(), firstPlayerId, 48);
        assertThrows(MatchNotFoundException.class, () ->matchScoreCalculatorService.updateScore(uuid, firstPlayerId));
    }




    MatchDto addPointManyTime(UUID uuid, int id, int count) {
        MatchDto matchDto = null;
        for (int i = 0; i < count; i++) {
            matchDto = matchScoreCalculatorService.updateScore(uuid, id);
        }
        return matchDto;

    }

}
