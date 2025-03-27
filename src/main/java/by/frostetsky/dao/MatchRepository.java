package by.frostetsky.dao;

import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.entity.Player;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MatchRepository implements Repository<Integer, FinishedMatch> {
    private final Session session;

    @Override
    public FinishedMatch save(FinishedMatch match) {
        session.save(match);
        return match;
    }

    @Override
    public Optional<FinishedMatch> findById(Integer id) {
        return Optional.ofNullable(session.find(FinishedMatch.class, id));
    }

    @Override
    public List<FinishedMatch> findAll() {
        var criteria = session.getCriteriaBuilder().createQuery(FinishedMatch.class);
        criteria.from(FinishedMatch.class);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public void update(FinishedMatch match) {
        session.update(match);
        session.flush();
    }

    @Override
    public void delete(Integer id) {
        session.remove(session.find(FinishedMatch.class, id));
        session.flush();
    }
}