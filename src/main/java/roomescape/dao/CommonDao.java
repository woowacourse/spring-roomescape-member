package roomescape.dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao<T> {

    List<T> findAll();

    Optional<T> findById(Long id);

    T insert(T t);

<<<<<<< cycle2
    int update(T t);

=======
>>>>>>> bee9827
    int delete(Long id);

    boolean existsById(Long id);
}
