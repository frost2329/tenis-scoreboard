package by.frostetsky.repository;

import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.exception.MatchRepositoryException;
import by.frostetsky.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Locale;

public class MatchRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public FinishedMatch save(FinishedMatch match) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(match);
            session.getTransaction().commit();
            return match;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new MatchRepositoryException("Error saving finished match", e);
        }
    }

    public List<FinishedMatch> findAll(Integer page, Integer size, String playerNameFilter) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        String hql = """
                SELECT m
                FROM FinishedMatch m
                WHERE lower(m.firstPlayer.name) LIKE :fragment
                OR lower(m.secondPlayer.name) LIKE :fragment
                """;
        try {
            transaction = session.beginTransaction();
            List<FinishedMatch> matches = session.createQuery(hql, FinishedMatch.class)
                    .setParameter("fragment", playerNameFilter != null ? playerNameFilter + "%" : "".toLowerCase(Locale.ROOT))
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
            session.getTransaction().commit();
            return matches;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new MatchRepositoryException("Error receiving matches", e);
        }
    }

    public Long getTotalCount() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        String hql = "select count(m) from FinishedMatch m";
        try {
            transaction = session.beginTransaction();
            Long totalCount = session.createQuery(hql, Long.class).uniqueResult();
            session.getTransaction().commit();
            return totalCount;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new MatchRepositoryException("Error receiving totalCount matches", e);
        }
    }
}
