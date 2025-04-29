package roomescape.time.repository;

import roomescape.time.entity.ReservationTimeEntity;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    ReservationTimeEntity save(ReservationTimeEntity entity);

    List<ReservationTimeEntity> findAll();

    boolean deleteById(Long id);

    Optional<ReservationTimeEntity> findById(Long id);
}
