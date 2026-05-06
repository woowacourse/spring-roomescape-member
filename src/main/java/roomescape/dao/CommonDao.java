package roomescape.dao;

import java.util.List;
import java.util.Optional;

public interface CommonDao<T> {
    default T getById(Long id, String name) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 %s 입니다.".formatted(name)));
    }

    List<T> findAll();

    Optional<T> findById(Long id);

    T insert(T t);

    int delete(Long id);
}
