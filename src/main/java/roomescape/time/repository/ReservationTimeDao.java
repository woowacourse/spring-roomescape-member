package roomescape.time.repository;

import roomescape.time.entity.ReservationTimeEntity;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeDao {
    ReservationTimeEntity save(ReservationTimeEntity entity);

    List<ReservationTimeEntity> findAll();

    boolean deleteById(Long id);

    Optional<ReservationTimeEntity> findById(Long id);
}
