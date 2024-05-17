package roomescape.time.domain;

import java.util.List;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime findByTimeId(Long timeId);

    ReservationTime save(ReservationTime reservationTime);

    int deleteById(Long id);
}
