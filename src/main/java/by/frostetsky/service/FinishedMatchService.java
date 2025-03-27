package by.frostetsky.service;

import by.frostetsky.dao.MatchRepository;
import by.frostetsky.dao.PlayerRepository;
import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.entity.Player;
import by.frostetsky.model.CurrentMatchModel;
import by.frostetsky.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class FinishedMatchService {
    private static final FinishedMatchService INSTANCE = new FinishedMatchService();
    private FinishedMatchService() {}
    public static FinishedMatchService getInstance() {return INSTANCE;}

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();



    public FinishedMatch saveMatch (CurrentMatchModel currentMatch) {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
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
    }

    public List<FinishedMatch> findAllMatches () {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        MatchRepository matchRepository = new MatchRepository(session);
        List<FinishedMatch> matches = matchRepository.findAll();
        session.getTransaction().commit();
        return matches;
    }
}
