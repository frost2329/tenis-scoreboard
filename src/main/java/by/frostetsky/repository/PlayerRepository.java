package by.frostetsky.repository;

import by.frostetsky.entity.Player;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;


import java.util.List;
import java.util.Optional;


public class PlayerRepository extends BaseRepository<Integer, Player> {

    public PlayerRepository(Session session) {
        super(session, Player.class);
    }

    public Optional<Player> getByName(String name) {
        Optional<Player> player = session.createQuery(" select p from Player p where p.name = :name", Player.class)
                .setParameter("name", name)
                .uniqueResultOptional();
        return player;
    }
}
