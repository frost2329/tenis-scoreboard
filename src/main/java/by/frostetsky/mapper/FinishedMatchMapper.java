package by.frostetsky.mapper;

import by.frostetsky.dto.FinishedMatchDto;
import by.frostetsky.entity.FinishedMatch;

public class FinishedMatchMapper {
    private static final FinishedMatchMapper INSTANCE = new FinishedMatchMapper();
    private  FinishedMatchMapper() {}
    public static FinishedMatchMapper getInstance() {
        return INSTANCE;
    }
    public FinishedMatchDto toDto(FinishedMatch finishedMatch) {
        return new FinishedMatchDto(finishedMatch.getFirstPlayer().getName(),
                finishedMatch.getSecondPlayer().getName(),
                finishedMatch.getWinner().getName());
    }
}
