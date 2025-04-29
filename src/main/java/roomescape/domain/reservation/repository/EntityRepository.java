package roomescape.domain.reservation.repository;

import java.util.List;
import java.util.Optional;

public interface EntityRepository<T> {

    List<T> findAll();

    Optional<T> findById(Long id);

    T save(T reservation);

    void deleteById(Long id);
}
