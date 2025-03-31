package by.frostetsky.repository;

import by.frostetsky.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository <K extends Serializable, E extends BaseEntity<K>> implements Repository <K, E> {
    protected final Session session;
    private final Class<E> clazz;

    @Override
    public E save(E entity) {
        session.persist(entity);
        return entity;
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        var criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public void update(E entity) {
        session.merge(entity);
        session.flush();
    }

    @Override
    public void delete(K id) {
        session.remove(session.find(clazz, id));
        session.flush();
    }
}
