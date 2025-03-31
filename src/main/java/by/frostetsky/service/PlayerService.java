package by.frostetsky.service;

import by.frostetsky.dto.PlayerDto;
import by.frostetsky.entity.FinishedMatch;
import by.frostetsky.mapper.PlayerMapper;
import by.frostetsky.repository.MatchRepository;
import by.frostetsky.repository.PlayerRepository;
import by.frostetsky.entity.Player;
import by.frostetsky.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class PlayerService {
    private static final PlayerService INSTANCE = new PlayerService();
    private  PlayerService() {}
    public static PlayerService getInstance() {return INSTANCE;}

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final PlayerMapper playerMapper = PlayerMapper.getInstance();

    public PlayerDto getPlayer(String name) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            PlayerRepository playerRepository = new PlayerRepository(session);
            Optional<Player> maybePlayer = playerRepository.getByName(name);
            Player player = maybePlayer.orElse(createPlayer(name, playerRepository));
            session.getTransaction().commit();
            return playerMapper.toDto(player);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при получении игрока", e);
        }


    }

    public Player createPlayer(String name, PlayerRepository playerRepository) {
        try {
            Player player = Player.builder().name(name).build();
            playerRepository.save(player);
            return player;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании игрока", e);
        }

    }
}
