package by.frostetsky.mapper;

import by.frostetsky.dto.MatchDto;
import by.frostetsky.dto.PlayerScoreDto;
import by.frostetsky.model.CurrentMatchModel;

public class MatchMapper {
    private static final MatchMapper INSTANCE = new MatchMapper();
    private MatchMapper() {
    }
    public static MatchMapper getInstance() {
        return INSTANCE;
    }

    public MatchDto toDto(CurrentMatchModel matchModel) {
        MatchDto matchDto = new MatchDto(
                matchModel.getUuid(),
                new PlayerScoreDto(
                        matchModel.getFirstPlayerId(),
                        matchModel.getFirstPlayerName(),
                        matchModel.getFirstPlayerScore().getSets(),
                        matchModel.getFirstPlayerScore().getGames(),
                        !matchModel.isTieBreak()
                                ? matchModel.getFirstPlayerScore().getPoints().getValue()
                                : String.valueOf(matchModel.getFirstPlayerScore().getTieBreakPoints())),
                new PlayerScoreDto(
                        matchModel.getSecondPlayerId(),
                        matchModel.getSecondPlayerName(),
                        matchModel.getSecondPlayerScore().getSets(),
                        matchModel.getSecondPlayerScore().getGames(),
                        !matchModel.isTieBreak()
                                ? matchModel.getSecondPlayerScore().getPoints().getValue()
                                : String.valueOf(matchModel.getSecondPlayerScore().getTieBreakPoints())),
                matchModel.isTieBreak(),
                matchModel.isFinished()
        );
        return matchDto;
    }

}
