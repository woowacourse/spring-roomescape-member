package roomescape.dao;

import roomescape.entity.ReservationEntity;

import java.util.List;

public interface ReservationDao {
    ReservationEntity save(ReservationEntity entity);

    boolean deleteById(Long id);

    List<ReservationEntity> findAll();
}
