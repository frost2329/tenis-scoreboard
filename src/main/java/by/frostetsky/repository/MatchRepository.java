package by.frostetsky.repository;

import by.frostetsky.entity.FinishedMatch;
import org.hibernate.Session;
import java.util.List;
import java.util.Locale;


public class MatchRepository extends BaseRepository<Integer, FinishedMatch>  {

    public MatchRepository(Session session) {
        super(session, FinishedMatch.class);
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