package repository.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    void create(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    T getById(ID id);
    void update(T entity);
    boolean delete(ID id);
}