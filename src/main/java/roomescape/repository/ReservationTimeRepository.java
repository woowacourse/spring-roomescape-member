package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    ReservationTime findById(Long id);

    int deleteById(Long id);

    List<ReservationTime> findAll();

    boolean isStartTimeExists(LocalTime startTime);
}
