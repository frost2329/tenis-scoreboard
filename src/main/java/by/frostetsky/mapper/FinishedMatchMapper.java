package by.frostetsky.mapper;

import by.frostetsky.dto.FinishedMatchDto;
import by.frostetsky.entity.FinishedMatch;

public class FinishedMatchMapper {
    public FinishedMatchDto toDto(FinishedMatch finishedMatch) {
        return new FinishedMatchDto(finishedMatch.getFirstPlayer().getName(),
                finishedMatch.getSecondPlayer().getName(),
                finishedMatch.getWinner().getName());
    }
}
