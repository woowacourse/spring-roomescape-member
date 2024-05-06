package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;

public interface TimeDao {
    List<ReservationTime> findAll();

    ReservationTime findById(long id);

    long save(ReservationTime reservationTime);

    boolean existByTime(LocalTime time);

    void deleteById(Long id);
}
