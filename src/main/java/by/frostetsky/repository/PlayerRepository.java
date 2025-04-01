package by.frostetsky.repository;

import by.frostetsky.entity.Player;
import by.frostetsky.exception.PlayerRepositoryException;
import by.frostetsky.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class PlayerRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Player save(Player player) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(player);
            session.getTransaction().commit();
            return player;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new PlayerRepositoryException("Error saving user", e);
        }
    }

    public Optional<Player> findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Optional<Player> player = Optional.ofNullable(session.find(Player.class, id));
            session.getTransaction().commit();
            return player;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new PlayerRepositoryException("Error when receiving player", e);
        }
    }

    public Optional<Player> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Optional<Player> player = session.createQuery("select p from Player p where p.name = :name", Player.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
            session.getTransaction().commit();
            return player;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new PlayerRepositoryException("Error when receiving player by name", e);
        }

    }
}
