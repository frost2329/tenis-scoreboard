package by.frostetsky.service;

import by.frostetsky.dao.PlayerRepository;
import by.frostetsky.entity.Player;
import by.frostetsky.mapper.PlayerMapper;
import by.frostetsky.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class PlayerService {
    private static final PlayerService INSTANCE = new PlayerService();
    private  PlayerService() {}
    public static PlayerService getInstance() {return INSTANCE;}

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Player getPlayer(String name) {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        PlayerRepository playerRepository = new PlayerRepository(session);
        Optional<Player> maybePlayer = playerRepository.getByName(name);
        Player player = maybePlayer.orElse(createPlayer(name, playerRepository));
        session.getTransaction().commit();
        return player;
    }

    public Player createPlayer(String name, PlayerRepository playerRepository) {
        Player player = Player.builder().name(name).build();
        playerRepository.save(player);
        return player;
    }
}
