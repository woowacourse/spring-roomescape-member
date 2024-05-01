package roomescape.dao;

import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {
    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

    boolean existByStartAt(LocalTime startAt);

    ReservationTime save(ReservationTime reservationTime);

    boolean deleteById(Long id);
}
