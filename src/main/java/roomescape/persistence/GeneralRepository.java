package roomescape.persistence;

import java.util.List;

public interface GeneralRepository<T> {

    List<T> findAll();

    T findById(Long id);

    Long add(T t);

    void delete(Long id);
}
