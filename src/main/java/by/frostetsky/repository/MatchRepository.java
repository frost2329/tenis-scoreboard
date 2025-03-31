package by.frostetsky.repository;

import by.frostetsky.entity.FinishedMatch;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;


import java.util.List;
import java.util.Locale;
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

    public List<FinishedMatch> findAll(Integer page, Integer size, String playerNameFilter) {
        String hql = """
                SELECT m 
                FROM FinishedMatch m 
                WHERE lower(m.firstPlayer.name) LIKE :fragment OR lower(m.secondPlayer.name) LIKE :fragment 
                """;
        return session.createQuery(hql, FinishedMatch.class)
                .setParameter("fragment", playerNameFilter != null ? playerNameFilter : "" + "%".toLowerCase(Locale.ROOT))
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Long getTotalCount() {
        String hql = "select count(m) from FinishedMatch m";
        return session.createQuery(hql, Long.class).uniqueResult();
    }
}