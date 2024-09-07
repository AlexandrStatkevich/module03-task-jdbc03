package by.alst.jbdc.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    boolean update(E e);

    Optional<E> findByName(K k);

    List<E> findAll();

    E save(E e);

    boolean delete(K k);
}
