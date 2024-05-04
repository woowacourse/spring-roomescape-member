package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime.ReservationTime;

public interface ReservationTimeRepository {

    Long save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

   List<ReservationTime> findByIdsNotIn(List<Long> ids);

    void delete(Long id);

    Boolean existId(Long id);

    Boolean existTime(LocalTime time);
}
