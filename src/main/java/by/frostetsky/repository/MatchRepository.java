package by.frostetsky.repository;

import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.exception.MatchRepositoryException;
import by.frostetsky.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class MatchRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public FinishedMatch save(FinishedMatch match) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            log.info("Saving finished match in database {}", match);
            transaction = session.beginTransaction();
            session.save(match);
            session.getTransaction().commit();
            return match;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Exception occurred", e);
            throw new MatchRepositoryException("Error saving finished match", e);
        }
    }

    public List<FinishedMatch> findAll(Integer page, Integer size, String playerNameFilter) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        String hql = """
                SELECT m
                FROM FinishedMatch m
                WHERE lower(m.firstPlayer.name) LIKE lower(:fragment)
                OR lower(m.secondPlayer.name) LIKE lower(:fragment)
                """;
        String fragment = playerNameFilter != null ? playerNameFilter + "%" : "%";
        try {
            log.info("Getting all finished matches by params page={}, size{}, fragment={}",page, size, fragment);
            transaction = session.beginTransaction();
            List<FinishedMatch> matches = session.createQuery(hql, FinishedMatch.class)
                    .setParameter("fragment", fragment)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
            session.getTransaction().commit();
            return matches;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Exception occurred", e);
            throw new MatchRepositoryException("Error receiving matches", e);
        }
    }

    public Long getTotalCount(String playerNameFilter) {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
        select count(m)
        from FinishedMatch m 
        WHERE lower(m.firstPlayer.name) LIKE lower(:fragment)
        OR lower(m.secondPlayer.name) LIKE lower(:fragment)
        """;
        Transaction transaction = null;
        String fragment = playerNameFilter != null ? playerNameFilter + "%" : "%";
        try {
            log.info("Getting totalCount of finished matches");
            transaction = session.beginTransaction();
            Long totalCount = session.createQuery(hql, Long.class)
                    .setParameter("fragment", fragment)
                    .uniqueResult();
            session.getTransaction().commit();
            return totalCount;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Exception occurred", e);
            throw new MatchRepositoryException("Error receiving totalCount matches", e);
        }
    }
}
