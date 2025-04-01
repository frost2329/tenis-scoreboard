package by.frostetsky.service;

import by.frostetsky.dto.FinishedMatchesResponse;
import by.frostetsky.exception.FinishedMatchServiceException;
import by.frostetsky.repository.MatchRepository;
import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.mapper.FinishedMatchMapper;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.repository.PlayerRepository;


import java.util.List;

public class FinishedMatchService {
    private static final FinishedMatchService INSTANCE = new FinishedMatchService();
    private FinishedMatchService() {
    }
    public static FinishedMatchService getInstance() {
        return INSTANCE;
    }
    private final PlayerRepository playerRepository = new PlayerRepository();
    private final MatchRepository matchRepository = new MatchRepository();
    private final FinishedMatchMapper finishedMatchMapper = FinishedMatchMapper.getInstance();

    public FinishedMatch saveMatch(CurrentMatchModel currentMatch) {
        try {
            FinishedMatch match = FinishedMatch.builder()
                    .firstPlayer(playerRepository.findById(currentMatch.getFirstPlayerId()).orElseThrow())
                    .secondPlayer(playerRepository.findById(currentMatch.getSecondPlayerId()).orElseThrow())
                    .winner(playerRepository.findById(currentMatch.getWinner()).orElseThrow())
                    .build();
            matchRepository.save(match);
            return match;
        } catch (RuntimeException e) {
            throw new FinishedMatchServiceException("Error saving finished match in service", e);
        }
    }

    public FinishedMatchesResponse findAllMatches(Integer page, Integer size, String filter) {
        try {
            List<FinishedMatch> matches = matchRepository.findAll(page, size, filter);
            Long totalCount = matchRepository.getTotalCount();
            return new FinishedMatchesResponse(
                    matches.stream()
                            .map(finishedMatchMapper::toDto)
                            .toList(),
                    totalCount);
        } catch (RuntimeException e) {
            throw new FinishedMatchServiceException("Error receiving finished matches in service", e);
        }
    }
}
