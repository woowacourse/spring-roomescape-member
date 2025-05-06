package roomescape.domain;

import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeRepository {

    ReservationTime findById(Long id);

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    int deleteById(Long id);

    boolean existByTimeValue(LocalTime time);
}
