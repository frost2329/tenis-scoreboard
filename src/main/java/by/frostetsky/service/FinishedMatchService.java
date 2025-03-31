package by.frostetsky.service;

import by.frostetsky.dto.FinishedMatchesResponse;
import by.frostetsky.repository.MatchRepository;
import by.frostetsky.repository.PlayerRepository;
import by.frostetsky.dto.FinishedMatchDto;
import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.mapper.FinishedMatchMapper;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class FinishedMatchService {
    private static final FinishedMatchService INSTANCE = new FinishedMatchService();

    private FinishedMatchService() {
    }

    public static FinishedMatchService getInstance() {
        return INSTANCE;
    }

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final FinishedMatchMapper finishedMatchMapper = FinishedMatchMapper.getInstance();

    public FinishedMatch saveMatch(CurrentMatchModel currentMatch) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            PlayerRepository playerRepository = new PlayerRepository(session);
            MatchRepository matchRepository = new MatchRepository(session);
            FinishedMatch match = FinishedMatch.builder()
                    .firstPlayer(playerRepository.findById(currentMatch.getFirstPlayerId()).orElseThrow())
                    .secondPlayer(playerRepository.findById(currentMatch.getSecondPlayerId()).orElseThrow())
                    .winner(playerRepository.findById(currentMatch.getWinner()).orElseThrow())
                    .build();
            matchRepository.save(match);
            session.getTransaction().commit();
            return match;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при сохранении матча", e);
        }

    }

    public FinishedMatchesResponse findAllMatches(Integer page, Integer size, String filter) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            MatchRepository matchRepository = new MatchRepository(session);
            List<FinishedMatch> matches = matchRepository.findAll(page, size, filter);
            Long totalCount = matchRepository.getTotalCount();
            transaction.commit();
            return new FinishedMatchesResponse(
                    matches.stream()
                            .map(finishedMatchMapper::toDto)
                            .toList(),
                    totalCount);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при получении матча", e);
        }
    }
}
