package roomescape.time.repository;

import java.util.List;
import roomescape.time.entity.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime findById(Long id);

    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(Long id);
}
