package roomescape.infrastructure.dao;

import roomescape.domain.model.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeDao {

    ReservationTime findById(Long id);
    
    boolean existByTimeValue(LocalTime startAt);

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    int deleteById(Long id);
}
