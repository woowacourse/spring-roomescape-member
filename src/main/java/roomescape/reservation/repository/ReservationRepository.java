package roomescape.reservation.repository;

import roomescape.reservation.entity.ReservationEntity;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    ReservationEntity save(ReservationEntity entity);

    boolean deleteById(Long id);

    List<ReservationEntity> findAll();

    List<ReservationEntity> findAllByTimeId(Long id);

    Optional<ReservationEntity> findDuplicatedWith(ReservationEntity entity);
}
