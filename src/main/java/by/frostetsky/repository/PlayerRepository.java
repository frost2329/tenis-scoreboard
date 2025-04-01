package by.frostetsky.repository;

import by.frostetsky.entity.Player;
import by.frostetsky.exception.PlayerRepositoryException;
import by.frostetsky.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

@Slf4j
public class PlayerRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Player save(Player player) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            log.info("Saving player in database {}", player);
            session.save(player);
            session.getTransaction().commit();
            return player;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Exception occurred", e);
            throw new PlayerRepositoryException("Error saving user", e);
        }
    }

    public Optional<Player> findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            log.info("Getting player by id {}", id);
            Optional<Player> player = Optional.ofNullable(session.find(Player.class, id));
            session.getTransaction().commit();
            return player;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Exception occurred", e);
            throw new PlayerRepositoryException("Error when receiving player", e);
        }
    }

    public Optional<Player> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            log.info("Getting player by name {}", name);
            Optional<Player> player = session.createQuery("select p from Player p where p.name = :name", Player.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
            session.getTransaction().commit();
            return player;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Exception occurred", e);
            throw new PlayerRepositoryException("Error when receiving player by name", e);
        }

    }
}
