package roomescape.repository;

import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime findByTimeId(Long timeId);

    ReservationTime save(ReservationTime reservationTime);

    int deleteById(Long id);
}
