package by.frostetsky.dao;

import by.frostetsky.entity.Player;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlayerRepository implements Repository<Integer, Player> {
    private final Session entityManager;

    @Override
    public Player save(Player entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Player> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Player.class, id));
    }

    @Override
    public List<Player> findAll() {
        var criteria = entityManager.getCriteriaBuilder().createQuery(Player.class);
        criteria.from(Player.class);
        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public void update(Player entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }

    @Override
    public void delete(Integer id) {
        entityManager.remove(entityManager.find(Player.class, id));
        entityManager.flush();
    }

    public Optional<Player> getByName(String name) {
        Optional<Player> player = entityManager.createQuery(" select p from Player p where p.name = :name", Player.class)
                .setParameter("name", name)
                .uniqueResultOptional();
        return player;
    }
}
