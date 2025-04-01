package by.frostetsky.service;

import by.frostetsky.dto.FinishedMatchesResponse;
import by.frostetsky.exception.FinishedMatchServiceException;
import by.frostetsky.repository.MatchRepository;
import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.mapper.FinishedMatchMapper;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;


import java.util.List;

@Slf4j
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
            log.info("Finished match is crated {}", match);
            matchRepository.save(match);
            return match;
        } catch (RuntimeException e) {
            log.error("Exception occurred", e);
            throw new FinishedMatchServiceException("Error saving finished match in service", e);
        }
    }

    public FinishedMatchesResponse findAllMatches(Integer page, Integer size, String filter) {
        try {
            log.info("Getting all finished matches by params page={}, size{}, filter={}",page, size, filter);
            List<FinishedMatch> matches = matchRepository.findAll(page, size, filter);
            log.info("Getting totalCount of finished matches");
            Long totalCount = matchRepository.getTotalCount();
            return new FinishedMatchesResponse(
                    matches.stream()
                            .map(finishedMatchMapper::toDto)
                            .toList(),
                    totalCount);
        } catch (RuntimeException e) {
            log.error("Exception occurred", e);
            throw new FinishedMatchServiceException("Error receiving finished matches in service", e);
        }
    }
}
