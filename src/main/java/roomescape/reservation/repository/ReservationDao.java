package roomescape.reservation.repository;

import roomescape.reservation.entity.ReservationEntity;

import java.util.List;

public interface ReservationDao {
    ReservationEntity save(ReservationEntity entity);

    boolean deleteById(Long id);

    List<ReservationEntity> findAll();

    List<ReservationEntity> findAllByTimeId(Long id);
}
